package com.example.BookVault;

import com.example.BookVault.domain.Book;
import com.example.BookVault.domain.BookId;
import com.example.BookVault.domain.BookRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// todo: write tests for this class
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
    public boolean existsById(BookId bookId) {
        return books.stream()
                .anyMatch(book -> book.getId().equals(bookId));
    }

    @Override
    public void deleteById(BookId bookId) {
        books.removeIf(book -> book.getId().equals(bookId));
    }

}
