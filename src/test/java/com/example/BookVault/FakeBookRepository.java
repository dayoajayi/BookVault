package com.example.BookVault;

import com.example.BookVault.domain.Book;
import com.example.BookVault.domain.BookId;
import com.example.BookVault.domain.BookRepository;
import com.example.BookVault.domain.Isbn;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// todo: write tests for this class
@Repository
public class FakeBookRepository implements BookRepository {
    private final List<Book> books = new ArrayList<>();


    @Override
    public Book save(Book book) {
        books.add(book);
        return book;
    }


    @Override
    public List<Book> findAll() {
        return books;
    }

    @Override
    public Optional<Book> findById(BookId bookId) {
        return books.stream()
                .filter(book -> book.getId().equals(bookId))
                .findFirst();
    }

    @Override
    public Optional<Book> findByIsbn(Isbn isbn) {
        return books.stream()
                .filter(book -> book.getIsbn().equals(isbn))
                .findFirst();
    }

    @Override
    public boolean existsById(BookId bookId) {
        return books.stream()
                .anyMatch(book -> book.getId().equals(bookId));

    }

    @Override
    public boolean existsByIsbn(Isbn isbn) {
        return books.stream()
                .anyMatch(book -> book.getIsbn().equals(isbn));

    }

    @Override
    public void deleteById(BookId bookId) {
        books.removeIf(book -> book.getId().equals(bookId));
    }

}
