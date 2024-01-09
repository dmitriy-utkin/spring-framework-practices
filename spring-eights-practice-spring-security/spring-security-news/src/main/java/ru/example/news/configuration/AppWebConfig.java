package ru.example.news.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import ru.example.news.web.interceptor.UserRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ConditionalOnProperty(value = "app.validation", name = "enable", havingValue = "true")
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
