package com.example.BookVault.domain;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class BookIdTest {

    @org.junit.jupiter.api.Test
    void testEquals() {

        UUID uuid = UUID.randomUUID();
        UUID uuid2 = UUID.fromString(uuid.toString());

        BookId bookId1 = new BookId(uuid);
        BookId bookId2 = new BookId(uuid2);
        assertTrue(bookId1.equals(bookId2));
    }
}