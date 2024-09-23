package com.abcdedu_backend.common.page;

import com.abcdedu_backend.common.page.request.PagingRequest;
import com.abcdedu_backend.common.page.request.SortRequest;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;

@AllArgsConstructor
public class PageManager {
    private PagingRequest pagingRequest;
    private SortRequest sortRequest;

    public PageRequest makePageRequest() {
        pagingRequest.checkPageRequest();
        PageRequest pageRequest = PageRequest.of(
                pagingRequest.getPage()-1,  // 1부터 시작하는 페이지 값을 0 기반으로 변경
                pagingRequest.getSize(),
                sortRequest.toSort()
        );

        return pageRequest;
    }



}
