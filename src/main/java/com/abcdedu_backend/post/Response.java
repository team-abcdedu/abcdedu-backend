package com.abcdedu_backend.post;

import lombok.Builder;
import lombok.Data;

/**
 * API 결과 반환용 클래스
 * API 호출 결과를 T에 담아 감싸면 반환해야 하는 필드가 바껴도 외부 스펙은 바뀌지 않는다.
 * @param <T>
 */
@Data
@Builder
public class Response<T> {
    private String resultCode;
    private T result;

    public static <T> Response<T> success(T result) {
        return Response.<T>builder()
                .resultCode("SUCCESS")
                .result(result)
                .build();
    }

    public static <T> Response<T> error(T result) {
        return Response.<T>builder()
                .resultCode("ERROR")
                .result(result)
                .build();
    }
}
