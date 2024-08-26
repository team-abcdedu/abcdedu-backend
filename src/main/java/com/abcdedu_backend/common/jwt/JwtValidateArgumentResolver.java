package com.abcdedu_backend.common.jwt;

import com.abcdedu_backend.exception.ApplicationException;
import com.abcdedu_backend.exception.ErrorCode;
import com.abcdedu_backend.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

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
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String token = webRequest.getHeader(ACCESS_TOKEN_HEADER);

        if (token == null) {
            throw new ApplicationException(ErrorCode.TOKEN_NOT_FOUND);
        }

        Long memberId = jwtUtil.getMemberIdFromAccessToken(token);
        return memberId;
    }

}
