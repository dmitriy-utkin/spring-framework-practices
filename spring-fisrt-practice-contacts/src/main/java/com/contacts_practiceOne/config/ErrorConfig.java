package com.contacts_practiceOne.config;


//TODO: DI для property.configs

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Data
@Component
@PropertySource("classpath:application.properties")
public class ErrorConfig {

    @Value("${app.contacts.error.options.emptyContactBook}")
    private String emptyContactBook;
    @Value("${app.contacts.error.options.invalidFullInfo}")
    private String invalidFullInfo;
    @Value("${app.contacts.error.options.invalidFullName}")
    private String invalidFullName;
    @Value("${app.contacts.error.options.invalidPhoneNumber}")
    private String invalidPhoneNumber;
    @Value("${app.contacts.error.options.invalidEmail}")
    private String invalidEmail;
    @Value("${app.contacts.error.options.alreadyExist}")
    private String alreadyExist;
    @Value("${app.contacts.error.options.emailNotFound}")
    private String emailNotFound;
}
