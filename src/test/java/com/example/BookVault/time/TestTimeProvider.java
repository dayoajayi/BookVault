package com.example.BookVault.time;

import jakarta.transaction.Transactional;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDate;

public class TestTimeProvider implements TimeProvider {

    private final ApplicationEventPublisher events;

    public TestTimeProvider(ApplicationEventPublisher events) {
        this.events = events;
    }

    private LocalDate now = LocalDate.now();

    @Override
    public LocalDate now() {
        return now;
    }

    @Transactional
    public void setNow(LocalDate now) {
        events.publishEvent(new DateUpdatedEvent(now));
        this.now = now;
    }
}
