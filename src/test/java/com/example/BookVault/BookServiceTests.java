package com.example.BookVault;

import com.example.BookVault.catalog.BookNotFoundException;
import com.example.BookVault.catalog.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest (classes = {BookService.class, FakeBookRepository.class})
class BookServiceTests {
    private BookRepository bookRepository;
    private BookService bookService;
    private ApplicationEventPublisher eventPublisher;

    @BeforeEach
    void setUp() {
        bookRepository = new FakeBookRepository();
        bookService = new BookService(eventPublisher, bookRepository);
    }

    @Test
    void canAddBook() {
        // given
        BookId bookId = new BookId();
        Book book = new Book(bookId, "Excellent Advice for Living", "Kevin Kelly", new Isbn("978-0-59-365453-8"));

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
        BookId bookId = new BookId();
        Book bookToSave = new Book(bookId, "Domain Driven Design", "Eric Evans", new Isbn("978-0-13-218126-6"));

        Book savedBook = bookService.addBook(bookToSave);

        Book retrievedBook = bookService.getBookById(savedBook.getId())
                .orElseThrow(() -> new AssertionError("Book should be present"));

        assertNotNull(retrievedBook);
        assertEquals(bookId, retrievedBook.getId());
        assertEquals("Domain Driven Design", retrievedBook.getTitle());
    }

    @Test
    void canAddAndGetMultipleBooks() {
        BookId bookId1 = new BookId();
        Isbn isbn1 = new Isbn("978-0-32-112521-7");
        Book bookToSave1 = new Book(bookId1, "Domain Driven Design", " Eric Evans", isbn1);
        Book savedBook1 = bookService.addBook(bookToSave1);

        List<Book> savedBooks = bookService.getBooks();

        assertEquals(List.of(savedBook1), savedBooks);

        BookId bookId2 = new BookId();
        Isbn isbn2 = new Isbn("978-0-59-365453-3");
        Book bookToSave2 = new Book(bookId2, "TDD by Example", "Kent Beck", isbn2);
        Book savedBook2 = bookService.addBook(bookToSave2);

        savedBooks = bookService.getBooks();

        assertEquals(List.of(savedBook1, savedBook2), savedBooks);
    }

    @Test
    void canGetBooks() {
        // given

        BookId bookId1 = new BookId();
        Isbn isbn1 = new Isbn("978-0-59-365453-8");
        BookId bookId2 = new BookId();
        Isbn isbn2 = new Isbn("978-0-59-365452-1");
        Book firstBook = new Book(bookId1, "Tell My Horse", "Zora Neale Hurston", isbn1);
        Book secondBook = new Book(bookId2, "Excellent Advice for Living", "Kevin Kelly", isbn2);

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
        BookId bookId = new BookId();
        Isbn isbn = new Isbn("978-0-59-365453-8");
        Book sampleBook = new Book(bookId, "Tell My Horse", "Zora Neale Hurston", isbn);

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
        BookId bookId = new BookId();
        Isbn isbn = new Isbn("978-0-59-365453-8");
        Book existingBook = new Book(bookId, "The Three Agreements", "Don Miguel Ruiz", isbn);
        Book updatedBook = new Book(bookId, "The Four Agreements", "Don Miguel Ruiz", isbn);
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
        BookId bookId = new BookId();
        Isbn isbn = new Isbn("978-0-59-365453-8");
        Book updatedBook = new Book(bookId, "The Three Agreements", "Don Miguel Ruiz", isbn);

        // when / then
        assertThrows(BookNotFoundException.class, () -> bookService.updateBook(bookId, updatedBook));

    }

    @Test
    void canDeleteBookById() {
        // given
        BookId bookId = new BookId();
        Isbn isbn = new Isbn("978-0-59-365453-8");
        Book book = new Book(bookId, "Domain Driven Design", "Eric Evans", isbn);
        bookService.addBook(book);

        // when
        bookService.deleteBookById(bookId);

        // then
        assertTrue(bookService.getBookById(bookId).isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenBookNotFound() {
        // given
        BookId bookId = new BookId();

        // when & then
        assertThrows(BookNotFoundException.class, () -> bookService.deleteBookById(bookId));

    }
}
