package com.example.BookVault.borrowing.domain;

import com.example.BookVault.catalog.domain.BookNotFoundException;
import com.example.BookVault.catalog.domain.BookService;
import com.example.BookVault.catalog.domain.Isbn;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CheckoutLedgerService {

    private ArrayList<String> checkoutLedger;
    private BookService bookService;

    CheckoutLedgerService(BookService bookService) {
        checkoutLedger = new ArrayList<>();
        this.bookService = bookService;
    }

    public CheckoutLedger getCheckoutLedger() {
        return null;
    }

    public void checkoutBook(String isbn) {
        if (bookService.getBookByIsbn(new Isbn(isbn)).isEmpty()) {
            throw new BookNotFoundException(isbn);
        }

        if (checkoutLedger.contains(isbn)) {
            throw new BookAlreadyCheckedOutException(isbn);
        }
        checkoutLedger.add(isbn);
    }

    public CheckoutLedgerEntry getCheckoutLedgerEntry(String isbn) {
        return new CheckoutLedgerEntry(isbn, checkoutLedger.contains(isbn));
    }
}
