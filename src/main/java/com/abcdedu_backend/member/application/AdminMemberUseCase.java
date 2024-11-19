package com.abcdedu_backend.member.application;

import com.abcdedu_backend.member.adapter.in.dto.request.ChangeMemberRoleRequest;
import com.abcdedu_backend.member.adapter.in.dto.request.MemberSearchCondition;
import com.abcdedu_backend.member.adapter.in.dto.response.AdminSearchMemberResponse;
import com.abcdedu_backend.member.application.domain.MemberRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AdminMemberUseCase {
    Page<AdminSearchMemberResponse> searchMembers(Pageable pageable, MemberSearchCondition cond);

    void updateMembersRole(MemberRole roleName, List<ChangeMemberRoleRequest> requests);
}
