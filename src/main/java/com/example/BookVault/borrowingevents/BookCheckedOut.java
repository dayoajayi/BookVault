package com.example.BookVault.borrowingevents;

import org.jmolecules.event.annotation.DomainEvent;

import java.time.LocalDate;

@DomainEvent
public record BookCheckedOut(
        String isbn,
        LocalDate dueDate
) {
}
