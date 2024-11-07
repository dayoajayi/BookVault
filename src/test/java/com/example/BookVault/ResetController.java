package com.example.BookVault;

import com.example.BookVault.accounting.domain.AccountService;
import com.example.BookVault.borrowing.domain.CheckoutLedgerService;
import com.example.BookVault.catalog.domain.BookService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResetController {

    AccountService accountService;
    CheckoutLedgerService checkoutLedgerService;
    BookService bookService;

    public ResetController(
            AccountService accountService,
            CheckoutLedgerService checkoutLedgerService,
            BookService bookService
    ) {
        this.accountService = accountService;
        this.checkoutLedgerService = checkoutLedgerService;
        this.bookService = bookService;
    }

    @PostMapping("/reset")
    public void reset() {
        checkoutLedgerService.reset();
        accountService.reset();
        bookService.reset();
    }
}
