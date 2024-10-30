package com.abcdedu_backend.member.controller;

import com.abcdedu_backend.common.jwt.JwtValidation;
import com.abcdedu_backend.common.page.PageManager;
import com.abcdedu_backend.common.page.request.PagingRequest;
import com.abcdedu_backend.common.page.request.SortRequest;
import com.abcdedu_backend.common.page.response.PagedResponse;
import com.abcdedu_backend.member.dto.request.ChangeMemberRoleRequest;
import com.abcdedu_backend.member.dto.request.MemberSearchCondition;
import com.abcdedu_backend.member.dto.response.AdminSearchMemberResponse;
import com.abcdedu_backend.member.entity.MemberRole;
import com.abcdedu_backend.member.service.AdminMemberService;
import com.abcdedu_backend.utils.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/members")
@RequiredArgsConstructor
@Tag(name = "관리자 멤버 관리 기능")

public class AdminMemberController {

    private final AdminMemberService adminMemberService;
    @Operation(summary = "멤버 조회", description = "필터링을 통한 멤버 조회")
    @GetMapping
    public Response<PagedResponse<AdminSearchMemberResponse>> getmembersByCondition(PagingRequest pagingRequest, SortRequest sortRequest, @ModelAttribute MemberSearchCondition cond){
        Page<AdminSearchMemberResponse> responses = adminMemberService.searchMembers(new PageManager(pagingRequest, sortRequest).makePageRequest(), cond);
        return Response.success(PagedResponse.from(responses));
    }
    @Operation(summary = "멤버 일괄 등급 변경", description = "현재 버전은 새싹 <-> 학생 변경만 가능합니다.")
    @PatchMapping("/role/{roleName}")
    public Response<Void> changeMembersRole(@JwtValidation Long memberId, @PathVariable MemberRole roleName,  @RequestBody List<ChangeMemberRoleRequest> requests) {
        return Response.success();
    }

}

