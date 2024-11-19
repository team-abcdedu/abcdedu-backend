package com.abcdedu_backend.memberv2.application;

import com.abcdedu_backend.memberv2.adapter.in.dto.request.ChangeMemberRoleRequest;
import com.abcdedu_backend.memberv2.adapter.in.dto.request.MemberSearchCondition;
import com.abcdedu_backend.memberv2.adapter.in.dto.response.AdminSearchMemberResponse;
import com.abcdedu_backend.memberv2.application.domain.MemberRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AdminMemberUseCase {
    Page<AdminSearchMemberResponse> searchMembers(Pageable pageable, MemberSearchCondition cond);

    void updateMembersRole(MemberRole roleName, List<ChangeMemberRoleRequest> requests);
}
