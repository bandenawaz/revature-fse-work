package org.revature.bookstoreapi.repository;

import org.revature.bookstoreapi.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    // JpaRepository<EntityType, PrimaryKeyType>

    /*
    By default this interface provide methods like save(), findAll(), findById(),
    update(), delete(), and all
     */

    /* Custom Queries
    Spring Data reads the method names and generates the custom query for you
     */
    List<Book> findByAuthor(String author); //Select * from books where author = ?
    List<Book> findByGenre(String genre);  //Select * from books where genre = ?

    //SELECT * from books where price Between ? and ?
    List<Book> findByPriceBetween(BigDecimal min, BigDecimal max);

    boolean existsByIsbn(String isbn);

    // Native query with actual MYSQL Syntax
    @Query(value = "SELECT * from books where stock_quantity = 0", nativeQuery = true)
    List<Book> findOutOfStockBooks();



}
