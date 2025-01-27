package com.example.BookVault.accounting.domain;

import com.example.BookVault.accountingevents.AccountCurrent;
import com.example.BookVault.accountingevents.AccountDelinquentEvent;
import com.example.BookVault.borrowingevents.BookCheckedOut;
import com.example.BookVault.borrowingevents.BookReturnedEvent;
import com.example.BookVault.time.DateUpdatedEvent;
import jakarta.transaction.Transactional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.modulith.moments.DayHasPassed;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class AccountService {

    private final ApplicationEventPublisher events;
    Map<String, LocalDate> checkoutLedger = new HashMap<>();
    Double balance = 0.0;
    LocalDate now = LocalDate.now();

    public AccountService(ApplicationEventPublisher events) {
        this.events = events;
    }

    public Double balance() {
        return balance;
    }

    public void reset() {
        this.balance = 0.0;
        checkoutLedger.clear();
    }

    @Transactional
    public void pay(Double amount) {
        if (amount <= 0 || amount > balance) {
            throw new InvalidPaymentException("Amount must be positive");
        }

        Double startingBalance = balance;

        balance -= amount;

        if (balance < 20.0 && startingBalance >= 20.0) {
            signalAccountCurrent();
        }
    }


    @ApplicationModuleListener
    public void onBookCheckedOut(BookCheckedOut bookCheckedOutEvent) {
        checkoutLedger.put(bookCheckedOutEvent.isbn(), bookCheckedOutEvent.dueDate());
    }

    @ApplicationModuleListener
    public void onBookReturned(BookReturnedEvent bookReturnedEvent) {
        checkoutLedger.remove(bookReturnedEvent.isbn());
    }

    @ApplicationModuleListener
    void on(DayHasPassed event) {
        now = event.getDate();
        boolean accountCurrent = balance < 20.0;
        long overdueBooks = checkoutLedger
                .values()
                .stream()
                .map(dueDate -> {
                    System.out.printf("Due Date: %s%n", dueDate.toString());
                    return dueDate;
                })
                .filter(dueDate -> dueDate.isBefore(now))
                .count();

        System.out.printf("Today's date: %s%n", now);
        System.out.printf("Checked out books: %d%n", checkoutLedger.size());
        System.out.printf("Overdue books: %d%n", overdueBooks);

        balance += overdueBooks;

        System.out.printf("New balance: %s%n", balance);

        if (balance >= 20.0 && accountCurrent) {
            System.out.println("Account is delinquent");
            signalAccountDelinquent();
        }
    }


    @ApplicationModuleListener
    public void onDateUpdated(DateUpdatedEvent dateUpdatedEvent) {

        LocalDate previousDate = now;
        now = dateUpdatedEvent.date();

        // iterate through each of the entries in the checkout ledger
        long newFines = checkoutLedger
                .values()
                .stream()
                // if a book is overdue, calculate the fine and add it to the balance
                .filter(dueDate -> dueDate.isBefore(now))
                .mapToInt(dueDate -> {
                    LocalDate assessFineFrom = previousDate.isAfter(dueDate) ? previousDate : dueDate;
                    return assessFineFrom.until(now).getDays();
                })
                .sum();

        balance += newFines;
        if (balance >= 20.0) {
            signalAccountDelinquent();
        }
    }

    @Transactional
    public void signalAccountDelinquent() {
        events.publishEvent(new AccountDelinquentEvent("Account is delinquent"));
    }

    @Transactional
    public void signalAccountCurrent() {
        events.publishEvent(new AccountCurrent("Account is current"));
    }
}
