package com.example.BookVault.catalog.api;

import com.example.BookVault.catalog.BookApi;
import com.example.BookVault.catalog.domain.Isbn;
import com.example.BookVault.catalog.BookDTO;
import com.example.BookVault.catalog.domain.Book;
import com.example.BookVault.catalog.domain.BookService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookManager implements BookApi  {

    private BookService bookService;

    public BookManager(BookService bookService) {
        this.bookService = bookService;
    }

    @Override
    public Optional<BookDTO> getBookByIsbn(String isbn) {
        return bookService.getBookByIsbn(new Isbn(isbn)).map(this::toDto);
    }

    private BookDTO toDto(Book book) {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setTitle(book.getTitle());
        bookDTO.setAuthor(book.getAuthor());
        bookDTO.setIsbn(book.getIsbn().value());
        return bookDTO;
    }
}
