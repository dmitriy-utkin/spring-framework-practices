package com.example.fourth.configuration;

import com.example.fourth.web.interceptor.UserRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppWebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userRequestInterceptor());
    }

    @Bean
    public UserRequestInterceptor userRequestInterceptor() {
        return new UserRequestInterceptor();
    }
}
