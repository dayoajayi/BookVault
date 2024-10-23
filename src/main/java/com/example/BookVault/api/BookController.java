package com.example.BookVault.api;

import com.example.BookVault.domain.Book;
import com.example.BookVault.domain.BookId;
import com.example.BookVault.domain.BookService;
import com.example.BookVault.domain.Isbn;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable UUID id) {

        Optional<BookDTO> bookDto = bookService.getBookById(new BookId(id)).map(book -> {
            BookDTO bookDTO = new BookDTO();
            bookDTO.setTitle(book.getTitle());
            bookDTO.setAuthor(book.getAuthor());
            bookDTO.setIsbn(book.getIsbn().value());
            return bookDTO;
        });
        return bookDto.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping()
    public List<BookDTO> getBooks() {
        return bookService.getBooks().stream().map(book -> {
            BookDTO bookDTO = new BookDTO();
            bookDTO.setTitle(book.getTitle());
            bookDTO.setAuthor(book.getAuthor());
            bookDTO.setIsbn(book.getIsbn().value());
            return bookDTO;
        }).toList();
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable String id, @RequestBody BookDTO book) {
        bookService.updateBook(new BookId(UUID.fromString(id)), new Book(book.getTitle(), book.getAuthor(), new Isbn(book.getIsbn())));

        return ResponseEntity.ok(book);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BookDTO> deleteBook(@PathVariable UUID id) {
        bookService.deleteBookById(new BookId(id));
        return ResponseEntity.noContent().build();
    }
}
