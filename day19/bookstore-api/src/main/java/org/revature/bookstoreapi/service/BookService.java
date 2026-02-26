package org.revature.bookstoreapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.revature.bookstoreapi.exceptions.DuplicateResourceException;
import org.revature.bookstoreapi.exceptions.ResourceNotFoundException;
import org.revature.bookstoreapi.model.Book;
import org.revature.bookstoreapi.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookService {

    private final BookRepository bookRepository;

    // ── CREATE ──────────────────────────────────────────────────
    @Transactional   // If anything fails mid-way, the entire operation rolls back
    public Book createBook(Book book) {
        // Business rule: ISBN must be unique
        if (bookRepository.existsByIsbn(book.getIsbn())) {
            throw new DuplicateResourceException(
                    "Book with ISBN " + book.getIsbn() + " already exists");
        }
        Book saved = bookRepository.save(book);
        // Hibernate generates: INSERT INTO books (title, author, isbn, ...) VALUES (?, ?, ?, ...)
        log.info("Book created with id: {}", saved.getId());
        return saved;
    }

    // ── READ ALL ────────────────────────────────────────────────
    public List<Book> getAllBooks(String author, String genre) {
        if (author != null && genre != null) {
            return bookRepository.findByAuthorAndGenre(author, genre);
            // WHERE author = ? AND genre = ?
        } else if (author != null) {
            return bookRepository.findByAuthor(author);
            // WHERE author = ?
        } else if (genre != null) {
            return bookRepository.findByGenre(genre);
            // WHERE genre = ?
        }
        return bookRepository.findAll();
        // SELECT * FROM books
    }

    // ── READ ONE ────────────────────────────────────────────────
    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                // findById returns Optional<Book> — it may or may not have a value
                .orElseThrow(() -> new ResourceNotFoundException("Book", id));
        // If empty Optional → throw exception → GlobalExceptionHandler → 404
        // Hibernate: SELECT * FROM books WHERE id = ?
    }

    // ── UPDATE ──────────────────────────────────────────────────
    @Transactional
    public Book updateBookPrice(Long id, BigDecimal newPrice) {
        Book book = getBookById(id);   // throws 404 if not found
        book.setPrice(newPrice);
        return bookRepository.save(book);
        // Hibernate detects the price changed → UPDATE books SET price = ? WHERE id = ?
        // Only the changed column is updated — not the whole row
    }

    @Transactional
    public Book updateBook(Long id, Book updatedBook) {
        Book existing = getBookById(id);

        // Update only non-null fields (PATCH semantics)
        if (updatedBook.getTitle() != null)    existing.setTitle(updatedBook.getTitle());
        if (updatedBook.getPrice() != null)    existing.setPrice(updatedBook.getPrice());
        if (updatedBook.getGenre() != null)    existing.setGenre(updatedBook.getGenre());
        if (updatedBook.getPublishedDate() != null)    existing.setPublishedDate(updatedBook.getPublishedDate());
        if (updatedBook.getStockQuantity() != null) existing.setStockQuantity(updatedBook.getStockQuantity());

        return bookRepository.save(existing);
        // Hibernate: UPDATE books SET title=?, price=?, ... WHERE id=?
    }

    // ── DELETE ──────────────────────────────────────────────────
    @Transactional
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException("Book", id);
        }
        bookRepository.deleteById(id);
        // Hibernate: DELETE FROM books WHERE id = ?
    }

    // ── SEARCH ──────────────────────────────────────────────────
    public List<Book> getBooksByPriceRange(BigDecimal min, BigDecimal max) {
        return bookRepository.findByPriceBetween(min, max);
        // WHERE price BETWEEN ? AND ?
    }
}