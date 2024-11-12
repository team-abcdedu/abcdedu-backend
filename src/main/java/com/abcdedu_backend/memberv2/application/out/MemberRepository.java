package com.abcdedu_backend.memberv2.application.out;

import com.abcdedu_backend.memberv2.application.domain.Member;

import java.util.Optional;

public interface MemberRepository {

    void save(Member signUpMember);

    Optional<Member> findByEmail(String email);

    Optional<Member> findById(Long memberId);

    Member updatePassword(String email, String newEncodedPassword);

    Member updatePassword(Long memberId, String newEncodedPassword);

    Member updateMemberInfo(Long memberId, String name, String school, Long studentId, String uploadImageObjectKey);
}
