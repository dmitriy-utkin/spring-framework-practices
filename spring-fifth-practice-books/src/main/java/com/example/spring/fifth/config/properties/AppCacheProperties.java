package com.example.spring.fifth.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@ConfigurationProperties(prefix = "app.cache")
public class AppCacheProperties {

    private final List<String> cacheNames = new ArrayList<>();
    private final Map<String, CacheExpirationProperty> properties = new HashMap<>();
    private CacheType cacheType;

    public interface CacheNames {
        String DB_BOOKS = "dbBooks";
        String DB_BOOKS_BY_CATEGORY = "dbBooksByCategory";
        String DB_BOOK_BY_NAME_AND_AUTHOR = "dbBookByNameAndAuthor";
    }


    @Data
    public static class CacheExpirationProperty {
        private Duration expiry = Duration.ZERO;
    }

    public enum CacheType {
        REDIS
    }

}
