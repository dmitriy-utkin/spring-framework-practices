package com.example.spring.fifth.config;

import com.example.spring.fifth.config.properties.AppCacheProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableCaching
@EnableConfigurationProperties(AppCacheProperties.class)
public class CacheConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "app.redis-cache", name = "enable", havingValue = "true")
    public CacheManager redisCacheManager(AppCacheProperties appCacheProperties, LettuceConnectionFactory lettuceConnectionFactory) {
        var defaultConfig = RedisCacheConfiguration.defaultCacheConfig();
        Map<String, RedisCacheConfiguration> redisCaches = new HashMap<>();
        appCacheProperties.getCacheNames().forEach(
                cache -> redisCaches.put(cache, RedisCacheConfiguration.defaultCacheConfig().entryTtl(
                        appCacheProperties.getProperties().get(cache).getExpiry()
                ))
        );

        return RedisCacheManager.builder()
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(redisCaches)
                .build();
    }
}
