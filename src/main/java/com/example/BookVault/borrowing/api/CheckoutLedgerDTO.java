package com.example.BookVault.borrowing.api;

import com.example.BookVault.borrowing.domain.CheckoutLedgerEntry;
import com.example.BookVault.borrowing.domain.CheckoutStatus;

import java.time.LocalDate;

public record CheckoutLedgerDTO(LocalDate dueDate, CheckoutStatus checkoutStatus) {

    public static CheckoutLedgerDTO from(CheckoutLedgerEntry entry) {
        return new CheckoutLedgerDTO(entry.dueDate(), entry.checkoutStatus());
    }
}
