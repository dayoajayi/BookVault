package com.example.BookVault;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;


class BookVaultApplicationTests {
    @BeforeEach
    void setup() {
    }



    @Test
    void contextLoads() {

        ApplicationModules.of(BookVaultApplication.class).verify();
    }

}
