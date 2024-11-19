package com.abcdedu_backend.member.service;
import com.abcdedu_backend.exception.ApplicationException;
import com.abcdedu_backend.member.application.AdminMemberService;
import com.abcdedu_backend.member.domain.Member;
import com.abcdedu_backend.member.domain.MemberRole;
import com.abcdedu_backend.member.application.out.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminMemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private AdminMemberService target;

    @Test
    void 역할_변경_성공() {
        // given
        List<Long> ids = List.of(
                1L,
                2L
        );

        MemberRole newRole = MemberRole.STUDENT;
        List<Member> updatedMembers = List.of(
                new Member(1L, "user1", "user1@example.com", "password123", "School A", 1001L, "key1", newRole, null, null, null),
                new Member(2L, "user2", "user2@example.com", "password123", "School B", 1002L, "key2", newRole, null, null, null)
        );
        doReturn(updatedMembers.get(0)).when(memberRepository).updateMemberRole(1L, newRole);
        doReturn(updatedMembers.get(1)).when(memberRepository).updateMemberRole(2L, newRole);

        // when
        target.updateMembersRole(newRole, ids);

        // then
        verify(memberRepository, times(1)).updateMemberRole(1L, newRole);
        verify(memberRepository, times(1)).updateMemberRole(2L, newRole);
    }

    @Test
    void 관리자_역할로_변경_시_예외_발생() {
        // given
        List<Long> ids = List.of(
                1L
        );
        // when & then
        assertThrows(ApplicationException.class, () -> target.updateMembersRole(MemberRole.ADMIN, ids));

    }
}
