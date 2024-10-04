package com.example.BookVault.domain;

import lombok.extern.slf4j.Slf4j;
import java.util.List;

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

    public java.util.Optional<Book> getBookById(int bookId) {
        return bookRepository.findById(bookId);
    }

    public Book updateBook(Integer bookId, Book updatedBook) {
        return bookRepository.findById(bookId)
                .map(existingBookToUpdate -> {
                    existingBookToUpdate = new Book(existingBookToUpdate.getId(), updatedBook.getTitle(), updatedBook.getAuthor());
                    return bookRepository.save(existingBookToUpdate);
                })
                .orElseThrow(() -> new BookNotFoundException("Book with id " + bookId + " not found"));
    }

    public void deleteBookById(int bookId) {
        if (!bookRepository.existsById(bookId)) {
            throw new BookNotFoundException("Book with id" + bookId + " not found");
        }

        bookRepository.deleteById(bookId);
    }
}
