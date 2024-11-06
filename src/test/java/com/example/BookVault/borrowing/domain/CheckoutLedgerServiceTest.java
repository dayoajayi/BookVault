package com.example.BookVault.borrowing.domain;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.BookVault.TimeProvider;
import com.example.BookVault.catalog.BookApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

public class CheckoutLedgerServiceTest {

    @Mock
    private BookApi bookApi;

    @Mock
    private TimeProvider timeProvider;

    @InjectMocks
    private CheckoutLedgerService checkoutLedgerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCalculateDueDate30DaysAfterCurrentDate() {
        // Given
        String isbn = "978-0-32-150362-6";
        LocalDate fixedDate = LocalDate.parse("2024-11-01");
        LocalDate expectedDueDate = fixedDate.plusDays(30);

        // Set up the mock to return the fixed date
        when(timeProvider.now()).thenReturn(fixedDate);

        // When
        checkoutLedgerService.checkoutBook(isbn);
        CheckoutLedgerEntry entry = checkoutLedgerService.getCheckoutLedgerEntry(isbn);

        // Then
        assertNotNull(entry, "The checkout ledger entry should not be null");
        assertEquals(expectedDueDate, entry.dueDate(), "The due date should be 30 days after the fixed date");
    }
}