package org.revature.bookstoreapi.repository;

import org.revature.bookstoreapi.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    // ↑ JpaRepository<EntityType, PrimaryKeyType>
    // Just by extending this interface, Spring Data JPA provides:
    // save(), findById(), findAll(), deleteById(), count(), existsById()... and more
    // You don't write any SQL. You don't write any implementation. Zero.

    // ── DERIVED QUERIES ──────────────────────────────────────────
    // Spring Data reads the method name and generates the SQL for you

    List<Book> findByAuthor(String author);
    // → SELECT * FROM books WHERE author = ?

    List<Book> findByGenre(String genre);
    // → SELECT * FROM books WHERE genre = ?

    List<Book> findByPriceBetween(BigDecimal min, BigDecimal max);
    // → SELECT * FROM books WHERE price BETWEEN ? AND ?

    List<Book> findByAuthorAndGenre(String author, String genre);
    // → SELECT * FROM books WHERE author = ? AND genre = ?

    boolean existsByIsbn(String isbn);
    // → SELECT COUNT(*) > 0 FROM books WHERE isbn = ?

    // ── JPQL (Java-level SQL — works with class/field names) ─────
    @Query("SELECT b FROM Book b WHERE b.price <= :maxPrice ORDER BY b.price ASC")
    List<Book> findAffordableBooks(@Param("maxPrice") BigDecimal maxPrice);

    // ── Native SQL (actual MySQL syntax) ────────────────────────
    @Query(value = "SELECT * FROM books WHERE stock_quantity = 0", nativeQuery = true)
    List<Book> findOutOfStockBooks();
}