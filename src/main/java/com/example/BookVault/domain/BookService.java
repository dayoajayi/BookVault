package com.example.BookVault.domain;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book addBook(Book book) {
        return bookRepository.save(book);
    }

    public List<Book> getBooks() {
        return bookRepository.findAll();
    }

    public java.util.Optional<Book> getBookById(BookId bookId) {
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
}
