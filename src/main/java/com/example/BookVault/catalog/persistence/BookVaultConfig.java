package com.example.BookVault.catalog.persistence;

import com.example.BookVault.catalog.domain.BookRepository;
import com.example.BookVault.catalog.domain.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BookVaultConfig {

    @Bean
    public BookRepository bookRepository(BookJPARepository bookJPARepository) {
        return new DatabaseBookRepository(bookJPARepository);
    }
}
