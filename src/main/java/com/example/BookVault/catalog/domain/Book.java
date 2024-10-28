package com.example.BookVault.catalog.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.Assert;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Book {
    public BookId id;
    public String title;
    public String author;
    public Isbn isbn;

    public Book(String title, String author, Isbn isbn) {
        Assert.notNull(title, "title cannot be null");
        Assert.notNull(author, "author cannot be null");
        Assert.notNull(isbn, "isbn cannot be null");

        this.id = new BookId();
        this.title = title;
        this.author = author;
        this.isbn = isbn;
    }
}
