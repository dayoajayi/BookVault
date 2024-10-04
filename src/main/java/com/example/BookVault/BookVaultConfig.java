package com.example.BookVault;

import com.example.BookVault.domain.BookRepository;
import com.example.BookVault.domain.BookService;
import com.example.BookVault.persistence.BookJPARepository;
import com.example.BookVault.persistence.DatabaseBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BookVaultConfig {

    public BookService bookService(BookRepository bookRepository) {
        return new BookService(bookRepository);
    }

    public BookRepository bookRepository(@Autowired BookJPARepository bookJPARepository) {
        return new DatabaseBookRepository(bookJPARepository);
    }

}
