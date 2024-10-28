package com.example.BookVault.catalog.domain;

public record Isbn(String value) {
    public Isbn {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("ISBN cannot be null or blank");
        }
    }
}
