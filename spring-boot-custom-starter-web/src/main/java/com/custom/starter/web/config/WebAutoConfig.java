package com.custom.starter.web.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@AutoConfiguration
@PropertySource(value = {"classpath:/web.properties"})
@ComponentScan("com.custom.starter.web.*")
public class WebAutoConfig {

}
