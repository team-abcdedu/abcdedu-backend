package com.abcdedu_backend.member.application.out;

import com.abcdedu_backend.member.application.domain.Member;
import com.abcdedu_backend.member.application.domain.MemberRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface MemberRepository {

    Member save(Member signUpMember);

    Optional<Member> findByEmail(String email);

    Optional<Member> findById(Long memberId);

    Member updatePassword(String email, String newEncodedPassword);

    Member updatePassword(Long memberId, String newEncodedPassword);

    Member updateMemberInfo(Long memberId, String name, String school, Long studentId, String uploadImageObjectKey);

    Boolean checkDuplicateEmail(String toEmail);

    Page<Member> findAllByCondition(String school, Long aLong, String name, MemberRole role, Pageable pageable);

    Member updateMemberRole(Long id, MemberRole roleName);

    void deleteById(Long memberId);
}
