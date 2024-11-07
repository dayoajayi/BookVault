package com.example.BookVault.events;

import java.time.LocalDate;

public record BookCheckedOutEvent(
        String isbn,
        LocalDate dueDate
) {
}
