package com.example.BookVault.time;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestTimeConfiguration {

    @Bean
    public TestTimeProvider testTimeProvider(ApplicationEventPublisher events) {
        return new TestTimeProvider(events);
    }
}
