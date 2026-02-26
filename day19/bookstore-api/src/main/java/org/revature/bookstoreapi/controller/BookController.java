package org.revature.bookstoreapi.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.revature.bookstoreapi.model.Book;
import org.revature.bookstoreapi.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
@Slf4j
public class BookController {

    private final BookService bookService;

    // ════════════════════════════════════════════════════
    //  CREATE — POST /api/v1/books
    // ════════════════════════════════════════════════════
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


    // ════════════════════════════════════════════════════
    //  READ ALL — GET /api/v1/books
    //  with optional filters: ?author=X&genre=Y
    // ════════════════════════════════════════════════════
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks(
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String genre) {
        //   ↑
        //  @RequestParam extracts values from the URL query string
        //
        //  GET /api/v1/books                        → author=null, genre=null → all books
        //  GET /api/v1/books?author=Robert+Martin   → author="Robert Martin", genre=null
        //  GET /api/v1/books?genre=Programming      → author=null, genre="Programming"
        //  GET /api/v1/books?author=X&genre=Y       → both filters applied
        //
        //  required=false means: if the param is absent, Spring passes null
        //  If required=true (the default) and the param is missing → 400 Bad Request

        List<Book> books = bookService.getAllBooks(author, genre);
        return ResponseEntity.ok(books);
    }


    // ════════════════════════════════════════════════════
    //  SEARCH BY PRICE RANGE — GET /api/v1/books/search
    //  ?minPrice=10&maxPrice=50
    // ════════════════════════════════════════════════════
    @GetMapping("/search")
    public ResponseEntity<List<Book>> searchByPrice(
            @RequestParam(defaultValue = "0") BigDecimal minPrice,
            @RequestParam(defaultValue = "9999") BigDecimal maxPrice) {
        //   ↑
        //  defaultValue: if the param is absent, use this value instead
        //  This implies required=false automatically
        //
        //  GET /api/v1/books/search                     → 0 to 9999 (all books)
        //  GET /api/v1/books/search?maxPrice=30         → 0 to 30
        //  GET /api/v1/books/search?minPrice=20&maxPrice=50 → 20 to 50
        //
        //  Spring also converts the "20" string → BigDecimal automatically!
        //  If someone sends ?minPrice=abc → 400 Bad Request (type mismatch)

        return ResponseEntity.ok(bookService.getBooksByPriceRange(minPrice, maxPrice));
    }


    // ════════════════════════════════════════════════════
    //  READ ONE — GET /api/v1/books/{id}
    // ════════════════════════════════════════════════════
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(
            @PathVariable Long id) {
        //   ↑
        //  @PathVariable extracts a value from the URL path itself
        //
        //  GET /api/v1/books/1     → id = 1L
        //  GET /api/v1/books/42    → id = 42L
        //  GET /api/v1/books/abc   → 400 Bad Request (Spring can't convert "abc" to Long)
        //
        //  The variable name in @PathVariable must match the {placeholder} in @GetMapping
        //  GET /api/v1/books/{id}  →  @PathVariable Long id   ✓
        //  GET /api/v1/books/{bookId}  →  @PathVariable Long id   ✗ (name mismatch!)
        //  GET /api/v1/books/{bookId}  →  @PathVariable("bookId") Long id   ✓

        Book book = bookService.getBookById(id);  // throws 404 if not found
        return ResponseEntity.ok(book);
    }


    // ════════════════════════════════════════════════════
    //  FULL UPDATE — PUT /api/v1/books/{id}
    // ════════════════════════════════════════════════════
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(
            @PathVariable Long id,
            @RequestBody Book book) {
        //   ↑
        //  Combining @PathVariable and @RequestBody in one method is very common.
        //  The ID tells us WHICH book to update.
        //  The request body tells us WHAT to update it to.
        //
        //  PUT /api/v1/books/1
        //  Body: { "title": "Clean Code", "price": 29.99, ... ALL fields required }
        //
        //  PUT semantics: you send the entire replacement object.
        //  Any field you omit becomes null in the database.
        //  This is why PUT requires the full object.

        Book updated = bookService.updateBook(id, book);
        return ResponseEntity.ok(updated);
    }


    // ════════════════════════════════════════════════════
    //  PARTIAL UPDATE — PATCH /api/v1/books/{id}/price
    // ════════════════════════════════════════════════════
    @PatchMapping("/{id}/price")
    public ResponseEntity<Book> updatePrice(
            @PathVariable Long id,
            @RequestBody Map<String, BigDecimal> body) {
        //   ↑
        //  PATCH for a specific field — URL tells you WHAT field to update
        //  Using Map<String, BigDecimal> instead of creating a whole DTO class
        //
        //  PATCH /api/v1/books/1/price
        //  Body: { "price": 24.99 }
        //
        //  body.get("price") → 24.99
        //
        //  This is more precise than PUT — client only sends the changed field

        BigDecimal newPrice = body.get("price");
        Book updated = bookService.updateBookPrice(id, newPrice);
        return ResponseEntity.ok(updated);
    }


    // ════════════════════════════════════════════════════
    //  DELETE — DELETE /api/v1/books/{id}
    // ════════════════════════════════════════════════════
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(
            @PathVariable Long id) {
        //   ↑
        //  Same @PathVariable pattern — extract ID from the URL
        //
        //  DELETE /api/v1/books/1   → deletes book with id=1
        //  DELETE /api/v1/books/999 → 404 (service throws ResourceNotFoundException)
        //
        //  Return type is ResponseEntity<Void> because:
        //  204 No Content means we successfully deleted but have nothing to return.
        //  The body is literally empty. Void signals that to Java.

        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
        // HTTP 204 No Content — empty body
    }
}