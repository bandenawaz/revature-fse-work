package org.revature.bookstoreapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "books")
@Data
@Builder
@NoArgsConstructor @AllArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //This field becomes Primary key
    private Long id;
    @Column(nullable = false, length = 255)
    private String title;
    @Column(nullable = false, length = 255)
    private String author;
    @Column(unique = true, nullable = false, length = 255)
    private String isbn;
    @Column(precision = 10, scale = 2)
    private BigDecimal price;
    @Column(name = "published_date")
    private LocalDate publishedDate;
    @Column(nullable = false)
    private String genre;
    @Column(name = "stock_quantity")
    private Integer stockQuantity;


    /*
    Hibernate rwads every @Entity class and generates SQL query automatically
    create table books(
    id BIGINT NOT NULL AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    isbn VARCHAR(255) NOT NULL UNIQUE,
    price DECIMAL(10,2),
    published_date DATE,
    genre VARCHAR(255) NOT NULL
    stock_quantity INT,
    PRIMARY KEY(id)
    );
     */
}
