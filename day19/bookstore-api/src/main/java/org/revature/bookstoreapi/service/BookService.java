package org.revature.bookstoreapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.revature.bookstoreapi.exceptions.DuplicateResourceException;
import org.revature.bookstoreapi.exceptions.ResourceNotFoundException;
import org.revature.bookstoreapi.model.Book;
import org.revature.bookstoreapi.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    // CREATE
    @Transactional // if anything fails midway the entire operations roll back
    public Book createBook(Book book) {

        //BUSINESS RULE: ISBN must be unique
        if (bookRepository.existsByIsbn(book.getIsbn())) {
            throw new DuplicateResourceException(
                    "Book with ISBN "+book.getIsbn() + " already exists"
            );
        }
        Book savedBook = bookRepository.save(book);
        log.info("Book created with ID: {}", savedBook.getId());
        return savedBook;
    }

    //Lets fetch all books
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    //Fetch book by id
    public Book getBookById(Long id) {

        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book", id));

    }

    //update book
    @Transactional
    public Book updateBook(Long id, Book book) {
        Book existingBook = getBookById(id);
        if (book.getTitle() != null) {
            existingBook.setTitle(book.getTitle());
        }

        if (book.getPrice() != null) {
            existingBook.setPrice(book.getPrice());
        }
        if (book.getGenre() != null) {
            existingBook.setGenre(book.getGenre());
        }
        if (book.getStockQuantity() != null) {
            existingBook.setStockQuantity(book.getStockQuantity());
        }
        log.info("Book updated with ID: {}", existingBook.getId());
        return bookRepository.save(existingBook);
    }

    //DELETE
    @Transactional
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            log.error("Book with ID {} does not exist", id);
            throw new ResourceNotFoundException("Book", id);

        }
        bookRepository.deleteById(id);
    }
}
