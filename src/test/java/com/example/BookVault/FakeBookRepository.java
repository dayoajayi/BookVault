package com.example.BookVault;


import com.example.BookVault.domain.Book;
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
    public Optional<Book> findById(Integer integer) {
        return books.stream()
                .filter(book -> book.getId().equals(integer))
                .findFirst();
    }

    @Override
    public boolean existsById(int bookId) {
        return false;
    }

    @Override
    public void deleteById(int bookId) {

    }


    @Override
    public List<Book> findAll() {
        return books;
    }

}
