package com.example.fourth.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "app.user-settings")
public class AppUserConfig {

    private String userName;
    private String userHeader;
}
