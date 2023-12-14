package com.contacts_practiceOne.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@ComponentScan("com.contacts_practiceOne")
@PropertySource("classpath:application.properties")
@Configuration
public class AppConfig {
}
