package com.abcdedu_backend.memberv2.application;
import com.abcdedu_backend.exception.ApplicationException;
import com.abcdedu_backend.exception.ErrorCode;
import com.abcdedu_backend.memberv2.adapter.in.dto.request.ChangeMemberRoleRequest;
import com.abcdedu_backend.memberv2.adapter.in.dto.request.MemberSearchCondition;
import com.abcdedu_backend.memberv2.adapter.in.dto.response.AdminSearchMemberResponse;
import com.abcdedu_backend.member.entity.Member;
import com.abcdedu_backend.member.entity.MemberRole;
import com.abcdedu_backend.member.repository.AdminMemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminMemberService implements AdminMemberUseCase{

    private final AdminMemberRepository memberRepository;

    @Override
    public Page<AdminSearchMemberResponse> searchMembers(Pageable pageable, MemberSearchCondition cond) {
        Page<Member> members = memberRepository.findAllByCondition(
                cond.school(),
                cond.studentId(),
                cond.name(),
                cond.role(),
                pageable);
        log.info("필터에 맞는 member 조회 성공");
        return members.map(AdminSearchMemberResponse::fromMember);
    }

    @Transactional
    public void updateMembersRole(MemberRole roleName, List<ChangeMemberRoleRequest> requests) {
        checkIsAllowedRoleChange(roleName); // to관리자 변경 방지

        List<Long> ids = requests.stream()
                .map(ChangeMemberRoleRequest::memberId)
                .toList();
        List<Member> members = memberRepository.findAllByIdIn(ids);
        members.forEach(member -> member.updateRole(roleName));
    }

    private void checkIsAllowedRoleChange(MemberRole roleName) {
        if (roleName == MemberRole.ADMIN) {
            throw new ApplicationException(ErrorCode.TO_ADMIN_REQUEST_IS_NOT_ALLOWED);
        }
    }
}
