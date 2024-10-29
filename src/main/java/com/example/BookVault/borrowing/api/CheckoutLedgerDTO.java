package com.example.BookVault.borrowing.api;

import com.example.BookVault.borrowing.domain.CheckoutLedgerEntry;

public record CheckoutLedgerDTO(Boolean borrowed) {

    public static CheckoutLedgerDTO from(CheckoutLedgerEntry entry) {
        return new CheckoutLedgerDTO(entry.isBorrowed());
    }
}
