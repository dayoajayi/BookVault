package com.example.BookVault.accounting.api;

import com.example.BookVault.accounting.domain.AccountService;
import com.example.BookVault.accounting.domain.InvalidPaymentException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/account/balance")
    public ResponseEntity<AccountBalanceDTO> getAccountBalance() {
        double balance = accountService.balance();
        return ResponseEntity.ok(new AccountBalanceDTO(balance));
    }

    @PostMapping("/account/pay")
    public ResponseEntity<Void> payAccountBalance(@RequestBody AccountPaymentDTO accountPaymentDTO) {
        accountService.pay(accountPaymentDTO.amount());
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(InvalidPaymentException.class)
    public ResponseEntity<String> handleInvalidPaymentException(InvalidPaymentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

}
