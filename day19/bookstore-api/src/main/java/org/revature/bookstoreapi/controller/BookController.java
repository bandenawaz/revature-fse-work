package org.revature.bookstoreapi.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.revature.bookstoreapi.model.Book;
import org.revature.bookstoreapi.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/books")
@Slf4j
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    //create book -POST /api/v1/books
    @PostMapping
    public ResponseEntity<Book> createBook(
            @RequestBody Book book) {
        //   ↑
        //  @RequestBody tells Spring:
        //  "Read the raw JSON from the HTTP request body
        //   and deserialize it into a Book object using Jackson"
        //
        //  Client sends:
        //  POST /api/v1/books
        //  Content-Type: application/json
        //  {
        //    "title": "Clean Code",
        //    "author": "Robert Martin",
        //    "isbn": "978-0132350884",
        //    "price": 35.99,
        //    "genre": "Programming",
        //    "stockQuantity": 50
        //  }
        //
        //  Jackson reads the JSON string and populates each field of Book automatically.
        //  Jackson also converts "35.99" → BigDecimal automatically.
        //  If the JSON is malformed → 400 Bad Request (handled by GlobalExceptionHandler)

        Book created = bookService.createBook(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
        // Response: 201 Created
        // Body: { "id": 1, "title": "Clean Code", ... }
    }

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    //One Book by Id
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }
}
