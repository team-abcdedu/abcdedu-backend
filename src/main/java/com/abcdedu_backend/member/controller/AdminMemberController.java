package com.abcdedu_backend.member.controller;

import com.abcdedu_backend.common.jwt.JwtValidation;
import com.abcdedu_backend.common.page.request.PagingRequest;
import com.abcdedu_backend.common.page.request.SortRequest;
import com.abcdedu_backend.member.dto.request.MemberSearchCondition;
import com.abcdedu_backend.member.dto.response.AdminSearchMemberResponse;
import com.abcdedu_backend.utils.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/members")
@RequiredArgsConstructor
@Tag(name = "관리자 멤버 관리 기능")

public class AdminMemberController {
    @Operation(summary = "멤버 조회", description = "필터링, ")
    @GetMapping("/search")
    public Response<List<AdminSearchMemberResponse>> getmembersByCondition(@JwtValidation Long memberId, PagingRequest pagingRequest, SortRequest sortRequest, MemberSearchCondition cond){
        return Response.success(null);
    }

}

