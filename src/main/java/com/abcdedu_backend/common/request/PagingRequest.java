package com.abcdedu_backend.common.request;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.PageRequest;

/**
 * 페이징 요청을 처리하기 위한 공통 클래스
 */
@ParameterObject
@Getter
@AllArgsConstructor
public class PagingRequest{
    @Parameter(description = "페이지 번호(1부터 시작)(null이면 1)")
    private Integer page;
    @Parameter(description = "페이지 크기(null이면 10)")
    private Integer size;


    /**
     * PageRequest로 변환
     * 웹계층(프론트엔드)에서 [page]를 1부터 시작하도록 요청하므로, 스프링의 [PageRequest]로 변환할 때 1을 빼준다.
     */
    public PageRequest toPageRequest() {
        if (page == null || page < 1) {
            page = 1;
        }
        if (size == null || size<1 || size>100) {
            size = 10;
        }
        return PageRequest.of(page - 1, size);
    }

}
