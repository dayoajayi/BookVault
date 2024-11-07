package com.example.BookVault.borrowing.domain;

public class AccountDelinquentException extends RuntimeException {
    public AccountDelinquentException() {
        super("Account is delinquent");
    }
}
