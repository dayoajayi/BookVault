package com.example.BookVault;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
public class TestTimeController {

    private TestTimeProvider timeProvider;

    public TestTimeController(TestTimeProvider timeProvider) {
        this.timeProvider = timeProvider;
    }

    @PostMapping("/test-time/{now}")
    public void testTime(LocalDate now) {
        timeProvider.setNow(now);
    }
}
