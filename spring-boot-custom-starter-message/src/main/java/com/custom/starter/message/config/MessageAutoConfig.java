package com.custom.starter.message.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@AutoConfiguration
@PropertySource("classpath:/message.properties")
@ComponentScan("com.custom.starter.message")
public class MessageAutoConfig {
}
