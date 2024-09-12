package com.abcdedu_backend.common.jwt;

import com.abcdedu_backend.exception.ApplicationException;
import com.abcdedu_backend.exception.ErrorCode;
import com.abcdedu_backend.global.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtValidateArgumentResolver implements HandlerMethodArgumentResolver {
    private final String ACCESS_TOKEN_HEADER = "Authorization";

    private final JwtUtil jwtUtil;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasJwtValidationAnnotation = parameter.hasParameterAnnotation(JwtValidation.class);
        boolean hasLongType = parameter.getParameterType().equals(Long.class);
        return hasJwtValidationAnnotation && hasLongType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String header = webRequest.getHeader(ACCESS_TOKEN_HEADER);
        if (header == null) {
            throw new ApplicationException(ErrorCode.TOKEN_NOT_FOUND);
        }
        if (!header.startsWith("Bearer ")){
            throw new ApplicationException(ErrorCode.INVALID_ACCESS_TOKEN);
        }
        String rawToken = header.substring(7);

        return jwtUtil.getMemberIdFromAccessToken(rawToken);
    }

}
