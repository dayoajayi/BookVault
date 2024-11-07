package com.example.BookVault.time;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
public class TestTimeController {

    private final TestTimeProvider timeProvider;

    public TestTimeController(TestTimeProvider timeProvider) {
        this.timeProvider = timeProvider;
    }

    @PostMapping("/test-time/{now}")
    public void testTime(@PathVariable LocalDate now) {
        timeProvider.setNow(now);
    }
}
