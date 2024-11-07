package com.example.BookVault.accounting.api;

import com.example.BookVault.accounting.domain.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/account/balance")
    public ResponseEntity<AccountBalanceDTO> getAccountBalance() {
        double balance = accountService.calculateFine();
        return ResponseEntity.ok(new AccountBalanceDTO(balance));
    }
}
