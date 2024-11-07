package com.example.BookVault.accounting.api;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AccountBalanceDTO {
    private double balance;

    public AccountBalanceDTO(double balance) {
        this.balance = balance;
    }

}
