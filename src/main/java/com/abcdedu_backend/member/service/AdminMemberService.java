package com.abcdedu_backend.member.service;
import com.abcdedu_backend.exception.ApplicationException;
import com.abcdedu_backend.exception.ErrorCode;
import com.abcdedu_backend.member.dto.request.MemberSearchCondition;
import com.abcdedu_backend.member.dto.response.AdminSearchMemberResponse;
import com.abcdedu_backend.member.entity.Member;
import com.abcdedu_backend.member.repository.AdminMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminMemberService {

    private final AdminMemberRepository memberRepository;

    public Page<AdminSearchMemberResponse> searchMembers(Long memberId, Pageable pageable, MemberSearchCondition cond) {
        Member member = checkMember(memberId);
        checkAdminPermission(member);
        log.info("권한 확인 성공 : {}", member.getRole());

        Page<Member> members = memberRepository.findAllByCondition(
                cond.school(),
                cond.studentId(),
                cond.name(),
                cond.role(),
                pageable);

        log.info("필터에 맞는 member 조회 성공");
        return members.map(AdminSearchMemberResponse::fromMember);
    }

    private Member checkMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));
    }
    private void checkAdminPermission(Member member) {
        if (!member.isAdmin()){
            throw new ApplicationException(ErrorCode.ADMIN_VALID_PERMISSION);
        }
    }
}
