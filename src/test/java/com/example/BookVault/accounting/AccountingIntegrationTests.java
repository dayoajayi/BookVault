package com.example.BookVault.accounting;

import com.example.BookVault.borrowingevents.BookCheckedOut;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Profile;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.modulith.test.Scenario;

import java.time.LocalDate;

@ApplicationModuleTest
@Profile("test")
public class AccountingIntegrationTests {

    @Test
    void itEmitsAccountDelinquentWhenBalanceExceedsLimit(Scenario scenario) {
//        scenario
//                .publish(new BookCheckedOut("123", LocalDate.now().minusDays(51)))
//                .

    }
}
