package ru.example.news.configuration;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "app.user-settings")
@ConditionalOnProperty(value = "app.validation", name = "enable", havingValue = "true")
public class AppUserConfig {

    private String userName;
    private String userHeader;
}
