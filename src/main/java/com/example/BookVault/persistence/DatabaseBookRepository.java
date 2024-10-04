package com.example.BookVault.persistence;

import com.example.BookVault.domain.Book;
import com.example.BookVault.domain.BookRepository;
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
    public Optional<Book> findById(Integer bookId) {

        return bookJPARepository.findById(bookId)
                .map(this::toBook);
    }

    @Override
    public boolean existsById(int bookId) {
        return bookJPARepository.existsById(bookId);
    }

    @Override
    public void deleteById(int bookId) {
        bookJPARepository.deleteById(bookId);
    }

    private BookJPAEntity toEntity(Book book) {
        return new BookJPAEntity(book.getId(), book.getTitle(), book.getAuthor());
    }

    private Book toBook(BookJPAEntity entity) {
        return new Book(entity.getId(), entity.getTitle(), entity.getAuthor());
    }
}
