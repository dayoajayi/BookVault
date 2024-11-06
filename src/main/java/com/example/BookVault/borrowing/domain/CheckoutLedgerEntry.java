package com.example.BookVault.borrowing.domain;

import java.time.LocalDate;

public record CheckoutLedgerEntry(String isbn, LocalDate dueDate,  CheckoutStatus checkoutStatus) {
}
