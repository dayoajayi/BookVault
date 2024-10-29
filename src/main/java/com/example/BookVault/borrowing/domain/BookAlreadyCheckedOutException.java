package com.example.BookVault.borrowing.domain;

public class BookAlreadyCheckedOutException extends RuntimeException {
    public BookAlreadyCheckedOutException(String isbn) {
        super("Book with ISBN '%s' is already checked out".formatted(isbn));
    }
}
