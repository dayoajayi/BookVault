package com.example.BookVault;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeatureTestsConfiguration {

    @Bean
    public TestTimeProvider testTimeProvider() {
        return new TestTimeProvider();
    }

}
