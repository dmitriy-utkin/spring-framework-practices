package com.example.spring.fifth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@ConfigurationProperties(prefix = "app.upload")
public class UploadConfig {

    private boolean enable;
    private String dataPath;
    private int defaultBooksNumber;
}
