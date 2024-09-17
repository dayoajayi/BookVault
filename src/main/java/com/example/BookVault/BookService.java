package com.example.BookVault;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class BookService {

    @Autowired
    private BookRepository bookRepository;

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
                    existingBookToUpdate = new Book(existingBookToUpdate.getId(), updatedBook.getAuthor(), updatedBook.getTitle());
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
