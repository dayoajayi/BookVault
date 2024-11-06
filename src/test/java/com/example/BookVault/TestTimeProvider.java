package com.example.BookVault;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class TestTimeProvider implements TimeProvider {

    private LocalDate now = LocalDate.now();

    @Override
    public LocalDate now() {
        return now;
    }


    public void setNow(LocalDate now) {
        this.now = now;
    }

}
