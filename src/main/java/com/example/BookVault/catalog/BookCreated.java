package com.example.BookVault.catalog;

import org.jmolecules.event.annotation.DomainEvent;

@DomainEvent
public record BookCreated(String isbn) {
}
