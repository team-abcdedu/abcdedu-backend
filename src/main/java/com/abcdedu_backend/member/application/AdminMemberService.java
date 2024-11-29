package com.abcdedu_backend.member.application;
import com.abcdedu_backend.exception.ApplicationException;
import com.abcdedu_backend.exception.ErrorCode;
import com.abcdedu_backend.member.application.dto.MemberInfoDto;
import com.abcdedu_backend.member.application.dto.command.SearchMembersCommand;
import com.abcdedu_backend.member.domain.Member;
import com.abcdedu_backend.member.domain.MemberRole;
import com.abcdedu_backend.member.application.out.MemberRepository;
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

    private final MemberRepository memberRepository;

    @Override
    public Page<MemberInfoDto> searchMembers(Pageable pageable, SearchMembersCommand command) {
        Page<Member> members = memberRepository.findAllByCondition(
                command.school(),
                command.studentId(),
                command.name(),
                command.role(),
                pageable);
        log.info("필터에 맞는 member 조회 성공");
        return members.map(member -> MemberInfoDto.of(member));
    }

    @Transactional
    public void updateMembersRole(MemberRole roleName, List<Long> ids) {
        checkIsAllowedRoleChange(roleName); // to관리자 변경 방지
        ids.forEach(id -> memberRepository.updateMemberRole(id, roleName));
    }

    private void checkIsAllowedRoleChange(MemberRole roleName) {
        if (roleName == MemberRole.ADMIN) {
            throw new ApplicationException(ErrorCode.TO_ADMIN_REQUEST_IS_NOT_ALLOWED);
        }
    }
}
