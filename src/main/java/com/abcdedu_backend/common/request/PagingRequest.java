package com.abcdedu_backend.common.request;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

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
    @Parameter(description = "정렬할 필드 이름(null이면 정렬하지 않음)", example = "createdAt")
    private String sortBy;
    @Parameter(description = "정렬 방향(asc: 오름차순, desc: 내림차순, null이면 asc)", example = "desc")
    private String sortDirection;

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
        // 정렬 설정
        Sort sort = Sort.unsorted();  // 기본값: 정렬하지 않음
        if (sortBy != null && !sortBy.isEmpty()) {
            Sort.Direction direction = Sort.Direction.ASC; // 기본값: 오름차순
            if ("desc".equalsIgnoreCase(sortDirection)) {
                direction = Sort.Direction.DESC;  // 사용자가 "desc"를 요청하면 내림차순으로 변경
            }
            sort = Sort.by(direction, sortBy);  // 정렬 필드와 방향 설정
        }

        return PageRequest.of(page - 1, size, sort);  // 1부터 시작하는 페이지 값을 0 기반으로 변경
    }


}
