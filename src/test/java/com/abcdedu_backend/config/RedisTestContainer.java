package com.abcdedu_backend.config;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class RedisTestContainer implements BeforeAllCallback {

    private static final String REDIS_HOST_PATH = "spring.data.redis.host";
    private static final String REDIS_PORT_PATH = "spring.data.redis.port";
    private static final String REDIS_DOCKER_IMAGE = "redis:7.0.7-alpine";

    private static final GenericContainer<?> redisContainer = new GenericContainer<>(REDIS_DOCKER_IMAGE)
            .withExposedPorts(6379);

    @Override
    public void beforeAll(ExtensionContext context) {
        redisContainer.start();

        // Redis 연결 정보 설정
        System.setProperty(REDIS_HOST_PATH, redisContainer.getHost());
        System.setProperty(REDIS_PORT_PATH, redisContainer.getFirstMappedPort().toString());
    }
}