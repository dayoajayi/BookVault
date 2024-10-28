package com.example.BookVault.catalog.api;

import com.example.BookVault.catalog.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public ResponseEntity<BookDTO> addBook(@RequestBody BookDTO book) {
        bookService.addBook(new Book(book.getTitle(), book.getAuthor(), new Isbn(book.getIsbn())));
        return ResponseEntity.status(HttpStatus.CREATED).body(book);
    }

    @GetMapping("/{isbn}")
    public ResponseEntity<BookDTO> getBookByIsbn(@PathVariable String isbn) {

        return bookService
                .getBookByIsbn(new Isbn(isbn))
                .map(this::toDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping()
    public List<BookDTO> getBooks() {

        return bookService.getBooks().stream().map(this::toDto).toList();
    }

    @PutMapping("/{id}") // TODO change to update by ISBN
    public ResponseEntity<BookDTO> updateBook(@PathVariable String id, @RequestBody BookDTO book) {
        bookService.updateBook(new BookId(UUID.fromString(id)), new Book(book.getTitle(), book.getAuthor(), new Isbn(book.getIsbn())));

        return ResponseEntity.ok(book);
    }

    @DeleteMapping("/{id}") // TODO change to delete by ISBN
    public ResponseEntity<BookDTO> deleteBook(@PathVariable UUID id) {
        bookService.deleteBookById(new BookId(id));
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(BookAlreadyExistsException.class)
    public ResponseEntity<String> handleBookAlreadyExistsException(BookAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    private BookDTO toDto(Book book) {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setTitle(book.getTitle());
        bookDTO.setAuthor(book.getAuthor());
        bookDTO.setIsbn(book.getIsbn().value());
        return bookDTO;
    }
}
