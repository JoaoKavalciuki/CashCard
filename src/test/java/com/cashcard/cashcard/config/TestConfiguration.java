package com.cashcard.cashcard.config;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfiguration {

    @Bean
    public TestRestTemplate getTestRestTemplate(){
        return new TestRestTemplate();
    }
}
