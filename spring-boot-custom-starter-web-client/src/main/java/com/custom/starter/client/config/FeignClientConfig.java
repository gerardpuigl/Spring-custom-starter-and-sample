package com.custom.starter.client.config;

import feign.Retryer;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@EnableFeignClients
public class FeignClientConfig {
    @Bean
    @Primary
    public Retryer feignRetryer() {
        return new Retryer.Default(100, 1000, 3); // Retry 3 times with 100ms initial interval and 1000ms max interval
    }
}