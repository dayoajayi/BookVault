package com.example.BookVault.borrowing.domain;

public record CheckoutLedgerEntry(String isbn, boolean isBorrowed) {
}
