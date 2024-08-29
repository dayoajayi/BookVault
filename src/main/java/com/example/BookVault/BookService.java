package com.example.BookVault;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@AllArgsConstructor
public class BookService {

    private final BookRepository bookRepository;


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
                .map(existingBook -> {
                    Book bookToUpdate = new Book(existingBook.getId(), updatedBook.getAuthor(), updatedBook.getTitle());
                    return bookRepository.save(bookToUpdate);
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
