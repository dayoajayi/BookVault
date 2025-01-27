package com.example.BookVault.borrowing.domain;

import com.example.BookVault.accountingevents.AccountCurrent;
import com.example.BookVault.accountingevents.AccountDelinquentEvent;
import com.example.BookVault.borrowingevents.BookCheckedOut;
import com.example.BookVault.borrowingevents.BookReturnedEvent;
import com.example.BookVault.catalog.BookNotFoundException;
import com.example.BookVault.catalog.BookCreated;
import com.example.BookVault.time.DateUpdatedEvent;
import jakarta.transaction.Transactional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.modulith.moments.DayHasPassed;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CheckoutLedgerService {

    private final ApplicationEventPublisher events;

    private final Set<String> books = new HashSet<>();

    private final Map<String, LocalDate> checkoutLedger = new HashMap<>();
    private LocalDate now;
    private boolean accountDelinquent = false;

    CheckoutLedgerService(
            ApplicationEventPublisher events,
            Clock clock
    ) {
        this.events = events;
        this.now = LocalDate.now(clock);
    }

    public void reset() {
        checkoutLedger.clear();
        accountDelinquent = false;
    }

    public CheckoutLedger getCheckoutLedger() {

        return new CheckoutLedger(checkoutLedger
                .entrySet()
                .stream()
                .map(entry -> new CheckoutLedgerEntry(entry.getKey(), entry.getValue(), CheckoutStatus.CHECKED_OUT)) // TODO write a test that checks the status
                .collect(Collectors.toMap(CheckoutLedgerEntry::isbn, entry -> entry)));
    }

    @Transactional
    public void checkoutBook(String isbn) {

        validateBookExists(isbn);

        if (checkoutLedger.containsKey(isbn)) {

            throw new BookAlreadyCheckedOutException(isbn);
        }

        if (accountDelinquent) {
            throw new AccountDelinquentException();
        }

        LocalDate dueDate = now.plusDays(30);

        checkoutLedger.put(isbn, dueDate);
        events.publishEvent(new BookCheckedOut(isbn, dueDate));
    }

    public CheckoutLedgerEntry getCheckoutLedgerEntry(String isbn) {

        validateBookExists(isbn);

        LocalDate dueDate = checkoutLedger.get(isbn);

        if (dueDate == null) {
            return new CheckoutLedgerEntry(isbn, null, CheckoutStatus.AVAILABLE);
        }

        if (now.isAfter(dueDate)) {
            return new CheckoutLedgerEntry(isbn, dueDate, CheckoutStatus.OVERDUE);
        }

        return new CheckoutLedgerEntry(isbn, dueDate, CheckoutStatus.CHECKED_OUT);
    }


    @Transactional
    public void returnBook(String isbn) {

        validateBookExists(isbn);

        LocalDate entry = checkoutLedger.get(isbn);
        if (entry == null) {
            throw new BookNotCheckedOutException(isbn);
        }

        checkoutLedger.remove(isbn);
        events.publishEvent(new BookReturnedEvent(isbn));
    }

    private void validateBookExists(String isbn) {
        if (!books.contains(isbn)) {
            throw new BookNotFoundException(isbn);
        }
    }

    @ApplicationModuleListener
    void on(DateUpdatedEvent event) {
        now = event.date();
    }

    @ApplicationModuleListener
    void on(AccountDelinquentEvent event) {
        accountDelinquent = true;
    }

    @ApplicationModuleListener
    void on(AccountCurrent event) {
        accountDelinquent = false;
    }

    @ApplicationModuleListener
    void on(BookCreated event) {
        books.add(event.isbn());
    }

    @ApplicationModuleListener
    void on(DayHasPassed event) {
        now = event.getDate();
    }
}
