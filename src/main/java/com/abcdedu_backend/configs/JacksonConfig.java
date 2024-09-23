package com.abcdedu_backend.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.TimeZone;

@Configuration
public class JacksonConfig {
    /**
     * Dto 직렬화 시, 표준시간이 UTC로 적용되는 것을 전역으로 관리하기 위한 커스터마이징
     */
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setTimeZone(TimeZone.getTimeZone("Asia/Seoul")); // 한국 시간으로 변환한다.
        return objectMapper;
    }
}
