package com.example.BookVault.catalog.domain;

import com.example.BookVault.catalog.BookAlreadyExistsException;
import com.example.BookVault.catalog.BookNotFoundException;
import com.example.BookVault.catalog.BookCreated;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class BookService {

    private final ApplicationEventPublisher events;
    private final BookRepository bookRepository;

    public BookService(ApplicationEventPublisher events, BookRepository bookRepository) {
        this.events = events;
        this.bookRepository = bookRepository;
    }

    public void reset() {
        bookRepository.findAll().stream().map(Book::getId).forEach(bookRepository::deleteById);
    }

    @Transactional
    public Book addBook(Book book) {

        if (bookRepository.existsByIsbn(book.getIsbn())) {
            throw new BookAlreadyExistsException("Book with id %s already exists".formatted(book.getId()));
        }
        events.publishEvent(new BookCreated(book.getIsbn().value()));
        return bookRepository.save(book);
    }

    public List<Book> getBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> getBookById(BookId bookId) {
        return bookRepository.findById(bookId);
    }

    public Book updateBook(BookId bookId, Book updatedBook) {
        Isbn isbn = new Isbn("978-3-16-148410-0");
        return bookRepository.findById(bookId)
                .map(existingBookToUpdate -> {
                    existingBookToUpdate = new Book(existingBookToUpdate.getId(), updatedBook.getTitle(), updatedBook.getAuthor(), isbn);
                    return bookRepository.save(existingBookToUpdate);
                })
                .orElseThrow(() -> new BookNotFoundException("Book with id " + bookId + " not found"));
    }

    public void deleteBookById(BookId bookId) {

        if (!bookRepository.existsById(bookId)) {
            throw new BookNotFoundException("Book with id" + bookId + " not found");
        }

        bookRepository.deleteById(bookId);
    }

    public Optional<Book> getBookByIsbn(Isbn isbn) {
        return bookRepository.findByIsbn(isbn);
    }
}
