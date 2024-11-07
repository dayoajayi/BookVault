package com.example.BookVault.borrowing.api;

import com.example.BookVault.borrowing.domain.*;
import com.example.BookVault.catalog.BookNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CheckoutLedgerController {

    private final CheckoutLedgerService checkoutLedgerService;

    public CheckoutLedgerController(CheckoutLedgerService checkoutLedgerService) {
        this.checkoutLedgerService = checkoutLedgerService;
    }

    @PostMapping("books/{isbn}/checkout")
    public ResponseEntity<Void> checkoutBook(@PathVariable String isbn) {
        checkoutLedgerService.checkoutBook(isbn);
        return ResponseEntity.ok().build();
    }

    @PostMapping("books/{isbn}/return")
    public ResponseEntity<Void> returnBook(@PathVariable String isbn) {
        checkoutLedgerService.returnBook(isbn);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<CheckoutLedgerDTO>> getCheckoutLedger() {
        checkoutLedgerService.getCheckoutLedger();
        return ResponseEntity.ok(List.of());
    }

    @GetMapping("checkout-ledger/{isbn}")
    public ResponseEntity<CheckoutLedgerDTO> getCheckoutLedgerForBook(@PathVariable String isbn) {
        CheckoutLedgerEntry entry = checkoutLedgerService.getCheckoutLedgerEntry(isbn);
        return ResponseEntity.ok(CheckoutLedgerDTO.from(entry));
    }

    @ExceptionHandler(BookAlreadyCheckedOutException.class)
    public ResponseEntity<String> handleBookAlreadyCheckedOutException(BookAlreadyCheckedOutException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(AccountDelinquentException.class)
    public ResponseEntity<String> handleAccountDelinquentException(AccountDelinquentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(BookNotCheckedOutException.class)
    public ResponseEntity<String> handleBookNotCheckedOutException(BookNotCheckedOutException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<String> handleBookNotFoundException(BookNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
