package com.practice.spring.todo.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@ConfigurationProperties(prefix = "app.user")
public class UserConfig {

    private String id;
    private String username;
    private String email;

}
