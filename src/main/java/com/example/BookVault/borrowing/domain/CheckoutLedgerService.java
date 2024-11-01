package com.example.BookVault.borrowing.domain;

import com.example.BookVault.TimeProvider;
import com.example.BookVault.catalog.BookApi;
import com.example.BookVault.catalog.BookNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CheckoutLedgerService {

    private ArrayList<String> checkoutLedger;
    private BookApi bookApi;
    private TimeProvider timeProvider;

    CheckoutLedgerService(
            BookApi bookApi,
            TimeProvider timeProvider
    ) {
        checkoutLedger = new ArrayList<>();
        this.bookApi = bookApi;
        this.timeProvider = timeProvider;
    }

    public CheckoutLedger getCheckoutLedger() {
        return null;
    }

    public void checkoutBook(String isbn) {
        if (bookApi.getBookByIsbn(isbn).isEmpty()) {
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

    public void returnBook(String isbn) {
        if (bookApi.getBookByIsbn(isbn).isEmpty()) {
            throw new BookNotFoundException(isbn);
        }

        if (!checkoutLedger.contains(isbn)) {
            throw new BookNotCheckedOutException(isbn);
        }

        checkoutLedger.remove(isbn);
    }
}
