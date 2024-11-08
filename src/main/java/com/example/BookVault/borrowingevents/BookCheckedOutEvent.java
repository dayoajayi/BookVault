package com.example.BookVault.borrowingevents;

import java.time.LocalDate;

public record BookCheckedOutEvent(
        String isbn,
        LocalDate dueDate
) {
}
