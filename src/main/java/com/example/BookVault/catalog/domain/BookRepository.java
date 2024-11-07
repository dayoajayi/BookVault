package com.example.BookVault.catalog.domain;

import java.util.List;
import java.util.Optional;

public interface BookRepository {

    Book save(Book book);
    List<Book> findAll();
    Optional<Book> findById(BookId bookId);
    Optional<Book> findByIsbn(Isbn isbn);
    boolean existsById(BookId bookId);
    boolean existsByIsbn(Isbn isbn);
    void deleteById(BookId bookId);
}
