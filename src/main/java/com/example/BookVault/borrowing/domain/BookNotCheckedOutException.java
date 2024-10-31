package com.example.BookVault.borrowing.domain;

public class BookNotCheckedOutException extends IllegalStateException {
    public BookNotCheckedOutException(String isbn) {
        super("Book with ISBN %s is not checked out".formatted(isbn));
    }
}
