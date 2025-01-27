package com.example.BookVault.time;

import jakarta.transaction.Transactional;
import org.springframework.modulith.moments.support.TimeMachine;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.*;

@RestController
public class TestTimeController {

    private final TestTimeProvider timeProvider;
    private final SettableClock clock;
    private final TimeMachine timeMachine;

    public TestTimeController(TestTimeProvider timeProvider, SettableClock clock, TimeMachine timeMachine) {
        this.timeProvider = timeProvider;
        this.clock = clock;
        this.timeMachine = timeMachine;
    }

    @PostMapping("/test-time/{now}")
    public void testTime(@PathVariable LocalDate now) {
        timeProvider.setNow(now);
        clock.setDelegate(Clock.fixed(now.atStartOfDay().toInstant(ZoneOffset.UTC), ZoneId.of("UTC")));
    }

    @PostMapping("/shift-time/{days}")
    @Transactional
    public void shiftDays(@PathVariable Integer days) {
        timeMachine.shiftBy(Duration.ofDays(days));
    }
}
