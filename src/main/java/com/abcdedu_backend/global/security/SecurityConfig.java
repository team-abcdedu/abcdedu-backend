package com.abcdedu_backend.global.security;

import com.abcdedu_backend.exception.ErrorResponse;
import com.abcdedu_backend.member.domain.MemberRole;
import com.abcdedu_backend.utils.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.util.List;


@EnableWebSecurity
@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final RoleQueryFilter roleQueryFilter;
    private final JwtProvider jwtProvider;
    private final ObjectMapper objectMapper;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring()
            .requestMatchers("/v3/api-docs/**")
            .requestMatchers("/h2-console/**")
            .requestMatchers("/static/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        /**
         * JWT를 사용하므로 csrf, sessionManagement를 사용하지 않도록 설정한다.
         * 이는 JWT가 토큰 기반이기 때문에 서버에서 세션을 관리하지 않아도 되기 때문이다.
         * csrf 또한 사용하지 않는다. (세션을 사용하지 않기 떄문에 csrf 기능을 사용하지 않는다.)
         */
        http.csrf(AbstractHttpConfigurer::disable);
        http.sessionManagement((session) -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//        http.cors(
//            cors -> cors.configurationSource(corsConfigSource())
//        );

        /**
         * 인증, 인가 설정
         * requestMatchers() 메서드를 통해 특정 요청에 대한 인증, 인가 설정을 한다.
         * permitAll() 메서드를 통해 특정 요청에 대한 인증 없이 접근을 허용한다.
         *
         * anyRequest() 메서드를 통해 나머지 요청에 대한 인증 설정을 한다.
         * authenticated() 메서드를 통해 인증된 사용자만 접근 가능하도록 설정한다.
         */
        http.authorizeHttpRequests((authorize) -> authorize
            .requestMatchers(
                "/auth/**",
                "/swagger-ui/**",
                "/actuator/**"
            ).permitAll()
            .requestMatchers("/admin/**").hasRole(MemberRole.ADMIN.name()) // ADMIN 권한만 접근 가능
            .anyRequest().permitAll() // TODO
        );

        /**
         * 시큐리티 필터체인에서의 예외처리
         * authenticated() 메서드를 통해 인증된 사용자만 접근 가능하도록 설정한다.
         * 1. 인증 예외 처리 (인증되지 않은 사용자) 401
         * 2. 인가 예외 처리 (권한이 없는 사용자) 403
         */
        http.exceptionHandling(exception -> exception
                .authenticationEntryPoint((request, response, authException) ->
                        responseError(response, "UNAUTHORIZED", "인증이 필요합니다.", 401)
                )
                .accessDeniedHandler((request, response, accessDeniedException) ->
                        responseError(response, "FORBIDDEN", "권한이 없습니다.", 403)
                )
        );


        /**
         * JWT 인증 필터를 시큐리티 필터체인에 등록한다.
         * BasicAuthenticationFilter를 상속받아 구현한 AuthorizationJwtHeaderFilter를 등록한다.
         * 해당 필터는 Request Header에서 Authorization을 확인하여 JWT 토큰을 추출하고 Provider에게 인증을 요청한다.
         * JwtProvider은 JwtAuthenticationToken을 통해 인증을 시도한다.
         */
        http.addFilterBefore(jwtAuthenticationFilter(authenticationManager(), objectMapper), BasicAuthenticationFilter.class);
        http.addFilterAfter(roleQueryFilter, AuthorizationJwtHeaderFilter.class);

        return http.build();
    }

    private void responseError(
        HttpServletResponse response,
        String code,
        String message,
        int status
    ) throws IOException {
        ErrorResponse errorResponse = new ErrorResponse(code, message);
        var apiResponse = Response.error(errorResponse);
        var json = objectMapper.writeValueAsString(apiResponse);
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
        response.getWriter().flush();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(List.of(jwtProvider));
    }

    @Bean
    public AuthorizationJwtHeaderFilter jwtAuthenticationFilter(AuthenticationManager authenticationManager, ObjectMapper objectMapper) {
        return new AuthorizationJwtHeaderFilter(authenticationManager, objectMapper);
    }

//    @Bean
//    public CorsConfigurationSource corsConfigSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOriginPatterns(List.of("*"));    // 모든 요청 허용
//        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH",
//            "OPTIONS"));      // 모든 HTTP 메서드 허용
//        configuration.setAllowedHeaders(List.of("*"));    // 모든 헤더 허용
//        configuration.setExposedHeaders(Arrays.asList("Authorization", "Set-cookie"));
//        configuration.setAllowCredentials(true);    // 쿠키와 같은 자격 증명을 허용
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//
//        return source;
//    }


    /**
     * RoleHierarchy
     * ADMIN > STUDENT > BASIC
     */
    @Bean
    public RoleHierarchy roleHierarchy() {
        return RoleHierarchyImpl.withDefaultRolePrefix()
            .role(MemberRole.ADMIN.name()).implies(MemberRole.STUDENT.name())
            .role(MemberRole.STUDENT.name()).implies(MemberRole.BASIC.name())
            .build();

    }

    @Bean
    public MethodSecurityExpressionHandler methodSecurityExpressionHandler(RoleHierarchy roleHierarchy) {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setRoleHierarchy(roleHierarchy);
        return expressionHandler;
    }
}
