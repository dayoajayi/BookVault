package com.example.BookVault.catalog.persistence;

import com.example.BookVault.FakeBookRepository;
import com.example.BookVault.catalog.domain.Book;
import com.example.BookVault.catalog.domain.BookId;
import com.example.BookVault.catalog.domain.BookRepository;
import com.example.BookVault.catalog.domain.Isbn;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FakeBookRepositoryTest {

    @Test
    void canCheckBookExistsById() {

        BookRepository bookRepository = new FakeBookRepository();

        // given
        BookId bookId = new BookId();
        Isbn isbn = new Isbn("978-0-59-365453-8");
        Book sampleBook = new Book(bookId, "Tell My Horse", "Zora Neale Hurston", isbn);

        // when
        bookRepository.save(sampleBook);
        assertTrue(bookRepository.existsById(sampleBook.getId()));

    }
}
