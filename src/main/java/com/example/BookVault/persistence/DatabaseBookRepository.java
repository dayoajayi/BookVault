package com.example.BookVault.persistence;

import com.example.BookVault.domain.Book;
import com.example.BookVault.domain.BookId;
import com.example.BookVault.domain.BookRepository;
import com.example.BookVault.domain.Isbn;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
public class DatabaseBookRepository implements BookRepository {
    private final BookJPARepository bookJPARepository;

    public DatabaseBookRepository(BookJPARepository bookJPARepository) {
        this.bookJPARepository = bookJPARepository;
    }


    @Override
    public Book save(Book book) {
        return toBook(bookJPARepository.save(toEntity(book)));
    }

    @Override
    public List<Book> findAll() {
        return bookJPARepository.findAll().stream()
                .map(this::toBook)
                .toList();
    }

    @Override
    public Optional<Book> findById(BookId bookId) {

        return bookJPARepository.findById(bookId)
                .map(this::toBook);
    }

    @Override
    public boolean existsById(BookId bookId) {
        return bookJPARepository.existsById(bookId);
    }

    @Override
    public void deleteById(BookId bookId) {
        bookJPARepository.deleteById(bookId);
    }

    private BookJPAEntity toEntity(Book book) {
        return new BookJPAEntity(book.getId().id(), book.getTitle(), book.getAuthor(), book.getIsbn().value());
    }

    private Book toBook(BookJPAEntity entity) {
        return new Book(new BookId(entity.getId()), entity.getTitle(), entity.getAuthor(), new Isbn(entity.getIsbn()));
    }
}
