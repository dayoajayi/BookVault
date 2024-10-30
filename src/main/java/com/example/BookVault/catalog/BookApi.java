package com.example.BookVault.catalog;

import java.util.Optional;

public interface BookApi {

    Optional<BookDTO> getBookByIsbn(String isbn);
}
