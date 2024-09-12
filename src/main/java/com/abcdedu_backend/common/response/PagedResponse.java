package com.abcdedu_backend.common.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;
@NoArgsConstructor
@Getter
public class PagedResponse<T> {
    private List<T> content;      // 실제 데이터 목록
    private int currentPage;      // 현재 페이지 번호
    private int pageSize;         // 페이지 크기
    private long totalElements;   // 전체 데이터 수
    private int totalPages;       // 전체 페이지 수
    private boolean last;         // 마지막 페이지 여부

    public PagedResponse(List<T> content, int currentPage, int pageSize, long totalElements, int totalPages, boolean last) {
        this.content = content;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.last = last;
    }

    public static <T> PagedResponse<T> from(Page<T> target) {
        return new PagedResponse<>(
                target.getContent(),
                target.getNumber() + 1,
                target.getSize(),
                target.getTotalElements(),
                target.getTotalPages(),
                target.isLast());
    }
}
