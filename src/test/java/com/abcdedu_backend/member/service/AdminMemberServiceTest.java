package com.abcdedu_backend.member.service;
import com.abcdedu_backend.exception.ApplicationException;
import com.abcdedu_backend.member.entity.Member;
import com.abcdedu_backend.member.entity.MemberRole;
import com.abcdedu_backend.member.repository.AdminMemberRepository;
import com.abcdedu_backend.member.dto.request.ChangeMemberRoleRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminMemberServiceTest {

    @Mock
    private AdminMemberRepository memberRepository;

    @InjectMocks
    private AdminMemberService target;

    @Test
    void 역할_변경_성공() {
        // given
        List<ChangeMemberRoleRequest> requests = List.of(
                new ChangeMemberRoleRequest(1L),
                new ChangeMemberRoleRequest(2L)
        );

        MemberRole newRole = MemberRole.STUDENT;
        List<Member> members = List.of(
                new Member(1L, "user1", "user1@example.com", "password123", "School A", 1001L, "key1", MemberRole.BASIC,false,  null, null),
                new Member(2L, "user2", "user2@example.com", "password123", "School B", 1002L, "key2", MemberRole.BASIC,false, null, null)
        );

        // Mock 동작 정의: findAllByIdIn이 호출되면 members 반환
        when(memberRepository.findAllByIdIn(anyList())).thenReturn(members);

        // when
        target.updateMembersRole(newRole, requests);

        // then
        assertThat(members).allMatch(member -> member.getRole() == newRole);
    }

    @Test
    void 관리자_역할로_변경_시_예외_발생() {
        // given
        List<ChangeMemberRoleRequest> requests = List.of(
                new ChangeMemberRoleRequest(1L)
        );
        // when & then
        assertThrows(ApplicationException.class, () -> target.updateMembersRole(MemberRole.ADMIN, requests));

    }
}
