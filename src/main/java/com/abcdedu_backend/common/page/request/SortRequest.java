package com.abcdedu_backend.common.page.request;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Sort;

/**
 * 정렬 요청을 처리하기 위한 공통 클래스
 */
@Slf4j
@ParameterObject
@Getter
@AllArgsConstructor
public class SortRequest {

    @Parameter(description = "정렬할 필드 이름(null이면 createdAt)")
    private String sortBy;

    @Parameter(description = "정렬 방향(asc: 오름차순, desc: 내림차순, null이면 desc)")
    private String sortDirection;


    /**
     * Sort로 변환
     */
    public Sort toSort() {
        // 기본값 셋팅
        sortBy = (sortBy != null && !sortBy.isEmpty()) ? sortBy : "createdAt";
        Sort.Direction direction = Sort.Direction.DESC;
        // 사용자가 "asc"를 요청하면 오름차순으로 변경
        if ("asc".equalsIgnoreCase(sortDirection)) {
            direction = Sort.Direction.ASC;  // 사용자가 "asc"를 요청하면 오름차순으로 변경
        }
        log.info("정렬 방향 : {}, 정렬 필드 : {}", direction, sortBy);

       return Sort.by(direction, sortBy);
    }


}
