package com.example.BookVault.accounting.domain;

import com.example.BookVault.borrowing.domain.CheckoutLedgerEntry;
import com.example.BookVault.borrowing.domain.CheckoutLedgerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
//@ApplicationModule
public class AccountService {

    @Autowired
    private CheckoutLedgerService checkoutLedgerService;

    public double calculateFine() {
        double fine = 2222.2;
        Map<String, CheckoutLedgerEntry> entries = checkoutLedgerService.getCheckoutLedger().entries();

        for (CheckoutLedgerEntry entry : entries.values()) {
            if (entry.isOverdue()) {
                fine += entry.calculateFine();
            }
        }
        return fine;
    }
}
