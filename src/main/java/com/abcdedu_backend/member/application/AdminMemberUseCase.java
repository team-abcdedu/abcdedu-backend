package com.abcdedu_backend.member.application;

import com.abcdedu_backend.member.application.dto.MemberInfoDto;
import com.abcdedu_backend.member.application.dto.command.SearchMembersCommand;
import com.abcdedu_backend.member.domain.MemberRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AdminMemberUseCase {
    Page<MemberInfoDto> searchMembers(Pageable pageable, SearchMembersCommand cond);

    void updateMembersRole(MemberRole roleName, List<Long> ids);
}
