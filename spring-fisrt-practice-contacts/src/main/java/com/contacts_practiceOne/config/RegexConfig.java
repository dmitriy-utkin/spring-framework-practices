package com.contacts_practiceOne.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Data
@Component
@PropertySource("classpath:application.properties")
public class RegexConfig {

    @Value("${app.contacts.regex.fullName}")
    private String fullName;
    @Value("${app.contacts.regex.phoneNumber}")
    private String phoneNumber;
    @Value("${app.contacts.regex.email}")
    private String email;
}
