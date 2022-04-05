package com.example.webchat.redis.properties;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class RedisProperties {

    private final String redisHost;
    private final int redisPort;

    public RedisProperties(
        @Value("${spring.redis.host}") String redisHost,
        @Value("${spring.redis.port}") int redisPort) {

        this.redisHost = redisHost;
        this.redisPort = redisPort;
    }
}
