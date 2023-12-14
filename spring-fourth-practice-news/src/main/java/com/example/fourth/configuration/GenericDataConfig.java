package com.example.fourth.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "app.generic-data")
public class GenericDataConfig {

    private boolean enable;
    private String newsDataPath;
    private String usersDataPath;
    private String commentsDataPath;
    private int multiplier;
}
