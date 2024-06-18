package com.custom.starter.client.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@AutoConfiguration
@PropertySource(value = {"classpath:/webclient.properties"})
@ComponentScan("com.custom.starter.client")
public class WebClientAutoConfig {

}
