package com.example.BookVault.domain;

import org.springframework.util.Assert;

import java.util.UUID;

public record BookId(UUID id) {

    public BookId {
        Assert.notNull(id, "id cannot be null");
    }

    public BookId() {
        this(UUID.randomUUID());
    }
}
