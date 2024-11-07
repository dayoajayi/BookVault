package com.example.BookVault.borrowing.domain;

import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;


public record CheckoutLedgerEntry(String isbn, LocalDate dueDate,  CheckoutStatus checkoutStatus) {

    public boolean isOverdue() {
        return LocalDate.now().isAfter(dueDate);
    }

    public double calculateFine() {
        long daysOverdue = DAYS.between(dueDate, LocalDate.now());
        return daysOverdue * 1.0;
    }
}
