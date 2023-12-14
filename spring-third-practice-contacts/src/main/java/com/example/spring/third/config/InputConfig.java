package com.example.spring.third.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.input")
public class InputConfig {

    private int numberOfGenericContacts;
    private String emailPath;
    private String firstNamePath;
    private String lastNamePath;
    private String phonePath;

}
