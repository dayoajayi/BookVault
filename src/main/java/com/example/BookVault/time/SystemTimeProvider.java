package com.example.BookVault.time;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Profile("!test")
public class SystemTimeProvider implements TimeProvider {

    @Override
    public LocalDate now() {
        return LocalDate.now();
    }
}
