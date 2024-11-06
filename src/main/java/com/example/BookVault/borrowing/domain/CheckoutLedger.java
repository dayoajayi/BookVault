package com.example.BookVault.borrowing.domain;

import java.util.Map;

public record CheckoutLedger(Map<String, CheckoutLedgerEntry> entries) {

}
