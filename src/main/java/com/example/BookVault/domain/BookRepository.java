package com.example.BookVault.domain;

import java.util.List;
import java.util.Optional;

public interface BookRepository {

    Book save(Book book);
    List<Book> findAll();
    Optional<Book> findById(BookId bookId);
    boolean existsById(BookId bookId);
    void deleteById(BookId bookId);

}
