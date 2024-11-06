package com.example.BookVault.borrowing.domain;

import com.example.BookVault.TimeProvider;
import com.example.BookVault.catalog.BookApi;
import com.example.BookVault.catalog.BookNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class CheckoutLedgerService {

//    private final ArrayList<CheckoutLedgerEntry> checkoutLedger;
    private final Map<String, CheckoutLedgerEntry> checkoutLedger ;
    private final Map<String, LocalDate> checkoutLedgerTimed  = new HashMap<>();
    private final BookApi bookApi;
    private final TimeProvider timeProvider;

    CheckoutLedgerService(
            BookApi bookApi,
            TimeProvider timeProvider
    ) {
        checkoutLedger = new HashMap<>();
        this.bookApi = bookApi;
        this.timeProvider = timeProvider;
    }

    public CheckoutLedger getCheckoutLedger() {
        return new CheckoutLedger(checkoutLedger);
    }

    public void checkoutBook(String isbn) {
        if (bookApi.getBookByIsbn(isbn).isEmpty()) {
            throw new BookNotFoundException(isbn);
        }

        if (checkoutLedger.containsKey(isbn)) {

            throw new BookAlreadyCheckedOutException(isbn);
        }
        LocalDate dueDate = timeProvider.now().plusDays(30);

        checkoutLedger.put(isbn, new CheckoutLedgerEntry(isbn, dueDate, CheckoutStatus.CHECKED_OUT));
        checkoutLedgerTimed.put(isbn,  dueDate);
    }

    public CheckoutLedgerEntry getCheckoutLedgerEntry(String isbn) {
        LocalDate dueDate = checkoutLedgerTimed.get(isbn);

        if (dueDate == null) {
            throw new BookNotFoundException(isbn);
        }

        if (timeProvider.now().isAfter(dueDate)) {
            return new CheckoutLedgerEntry(isbn, dueDate, CheckoutStatus.OVERDUE);
        }

        return new CheckoutLedgerEntry(isbn, dueDate, CheckoutStatus.CHECKED_OUT);
    }


    public void returnBook(String isbn) {
        if (bookApi.getBookByIsbn(isbn).isEmpty()) {
            throw new BookNotFoundException(isbn);
        }

        CheckoutLedgerEntry entry = checkoutLedger.get(isbn);
        if (entry == null || entry.checkoutStatus() != CheckoutStatus.CHECKED_OUT) {
            throw new BookNotCheckedOutException(isbn);
        }

        // Update the entry status to RETURNED
        checkoutLedger.put(isbn, new CheckoutLedgerEntry(isbn, entry.dueDate(), CheckoutStatus.RETURNED));
    }
}
