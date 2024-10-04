package com.example.BookVault;

import com.example.BookVault.domain.Book;
import com.example.BookVault.domain.BookNotFoundException;
import com.example.BookVault.domain.BookRepository;
import com.example.BookVault.domain.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest (classes = {BookService.class, FakeBookRepository.class})
class BookServiceTests {
    private BookRepository bookRepository;
    private BookService bookService;

    @BeforeEach
    void setUp() {
        bookRepository = new FakeBookRepository();
        bookService = new BookService(bookRepository);
    }

    @Test
    void canAddBook() {
        // given
        Book book = new Book(123, "Excellent Advice for Living", "Kevin Kelly");

        // when
        Book savedBook = bookService.addBook(book);

        // then
        assertAll(
                () -> assertNotNull(savedBook),
                () -> assertEquals("Excellent Advice for Living", savedBook.getTitle()),
                () -> assertEquals("Kevin Kelly", savedBook.getAuthor())
        );
    }

    @Test
    void canAddAndGetBooks() {
        Book bookToSave = new Book(123, "Domain Driven Design", " Eric Evans");

        Book savedBook = bookService.addBook(bookToSave);

        Book retrievedBook = bookService.getBookById(savedBook.getId())
                .orElseThrow(() -> new AssertionError("Book should be present"));

        assertNotNull(retrievedBook);
        assertEquals(123, retrievedBook.getId());
        assertEquals("Domain Driven Design", retrievedBook.getTitle());
    }

    @Test
    void canAddAndGetMultipleBooks() {
        Book bookToSave1 = new Book(123, "Domain Driven Design", " Eric Evans");
        Book savedBook1 = bookService.addBook(bookToSave1);

        List<Book> savedBooks = bookService.getBooks();

        assertEquals(List.of(savedBook1), savedBooks);

        Book bookToSave2 = new Book(456, "TDD by Example", "Kent Beck");
        Book savedBook2 = bookService.addBook(bookToSave2);

        savedBooks = bookService.getBooks();

        assertEquals(List.of(savedBook1, savedBook2), savedBooks);
    }

    @Test
    void canGetBooks() {
        // given
        Book firstBook = new Book(123, "Tell My Horse", "Zora Neale Hurston");
        Book secondBook = new Book(456, "Excellent Advice for Living", "Kevin Kelly");

        // add books
        bookService.addBook(firstBook);
        bookService.addBook(secondBook);

        // when
        List<Book> retrievedBooks = bookService.getBooks();

        // then
        assertAll(
                () -> assertNotNull(retrievedBooks, "The list of books should not be null"),
                () -> assertFalse(retrievedBooks.isEmpty(), "The list of books should not be empty"),
                () -> assertEquals(2, retrievedBooks.size()),
                () -> assertEquals("Tell My Horse", firstBook.getTitle())
        );
    }

    @Test
    void canGetBookById() {
        // given
        int bookId = 123;
        Book sampleBook = new Book(bookId, "Tell My Horse", "Zora Neale Hurston");

        // when
        bookService.addBook(sampleBook);
        Book retrievedBook = bookService
                .getBookById(sampleBook.getId())
                .orElseThrow(() -> new AssertionError("Book should be present"));

        // then
        assertAll(
                () -> assertEquals(sampleBook.getId(), retrievedBook.getId(), "Book ID should match"),
                () -> assertEquals(sampleBook.getTitle(), (retrievedBook.getTitle()), "Book Title should match"),
                () -> assertEquals(sampleBook.getAuthor(), (retrievedBook.getAuthor()), "Book Author should match")
        );
    }

    @Test
    void canUpdateBook() {
        // given
        int bookId = 1;
        Book existingBook = new Book(bookId, "The Three Agreements", "Don Miguel Ruiz");
        Book updatedBook = new Book(bookId, "The Four Agreements", "Don Miguel Ruiz");
        bookService.addBook(existingBook);

        // when
        Book result = bookService.updateBook(bookId, updatedBook);

        // then
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals("The Four Agreements", result.getTitle()),
                () -> assertEquals("Don Miguel Ruiz", result.getAuthor())
        );
    }

    @Test
    void updateBookFailsWhenBookNotFound() {

        // given
        int bookId = 1;
        Book updatedBook = new Book(bookId, "The Three Agreements", "Don Miguel Ruiz");

        // when / then
        assertThrows(BookNotFoundException.class, () -> bookService.updateBook(bookId, updatedBook));

    }

    @Test
    @Disabled
    void canDeleteBookById() {
        // given
        int bookId = 123;
        Book book = new Book(bookId, "Domain Driven Design", "Eric Evans");
        bookService.addBook(book);

        // when
        bookService.deleteBookById(bookId);

        // then
        // todo: this test failing. What's the correct assertion to make here??
        assertTrue(bookService.getBookById(bookId).isEmpty());
        assertThrows(BookNotFoundException.class, () -> bookService.getBookById(bookId));
    }

    @Test
    void shouldThrowExceptionWhenBookNotFound() {
        // given
        int bookId = 123;

        // when & then
        assertThrows(BookNotFoundException.class, () -> bookService.deleteBookById(bookId));

    }
}