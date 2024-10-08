package com.abcdedu_backend.configs;

import com.abcdedu_backend.common.jwt.JwtValidateArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final JwtValidateArgumentResolver jwtValidateArgumentResolver;


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000",
                        //Swagger 및 블루그린 배포용 COrs설정
                        "http://localhost:8080",
                        "http://localhost:8081",
                        "http://localhost:8082",
                        "https://abcdedu.com",
                        "https://www.abcdedu.com",
                        "https://dev.abcdedu.com",
                        "https://dev-api.abcdedu.com",
                        "https://api.abcdedu.com"
                        )
                .allowedHeaders("*")
                .allowedMethods(
                        HttpMethod.GET.name(),
                        HttpMethod.POST.name(),
                        HttpMethod.PATCH.name(),
                        HttpMethod.PUT.name(),
                        HttpMethod.DELETE.name())
                .allowCredentials(true)
                .maxAge(3600L);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(jwtValidateArgumentResolver);
    }

}
