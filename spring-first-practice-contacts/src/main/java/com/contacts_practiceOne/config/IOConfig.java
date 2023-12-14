package com.contacts_practiceOne.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Data
@Component
@PropertySource("classpath:application.properties")
public class IOConfig {

    @Value("${app.contacts.iopath.output}")
    private String output;

    @Value("${app.contacts.iopath.input}")
    private String input;
}
