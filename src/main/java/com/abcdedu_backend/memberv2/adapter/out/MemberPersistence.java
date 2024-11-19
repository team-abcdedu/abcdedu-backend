package com.abcdedu_backend.memberv2.adapter.out;

import com.abcdedu_backend.exception.ApplicationException;
import com.abcdedu_backend.exception.ErrorCode;
import com.abcdedu_backend.memberv2.adapter.out.entity.MemberEntity;
import com.abcdedu_backend.memberv2.application.domain.Member;
import com.abcdedu_backend.memberv2.application.domain.MemberRole;
import com.abcdedu_backend.memberv2.application.out.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@AllArgsConstructor
@Repository
public class MemberPersistence implements MemberRepository {

    private final MemberJpaRepository memberJpaRepository;

    @Override
    public Member save(Member member) {
        MemberEntity savedMember = memberJpaRepository.save(MemberEntity.from(member));
        return savedMember.toDomain();
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        return memberJpaRepository.findByEmail(email).map(MemberEntity::toDomain);
    }

    @Override
    public Optional<Member> findById(Long memberId) {
        return memberJpaRepository.findById(memberId).map(MemberEntity::toDomain);
    }

    @Override
    public Member updatePassword(String email, String newEncodedPassword) {
        MemberEntity member = memberJpaRepository.findByEmail(email).orElseThrow(() -> new ApplicationException(ErrorCode.EMAIL_NOT_FOUND));
        member.updatePassword(newEncodedPassword);
        return member.toDomain();
    }

    @Override
    public Member updatePassword(Long memberId, String newEncodedPassword) {
        MemberEntity member = memberJpaRepository.findById(memberId).orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));
        member.updatePassword(newEncodedPassword);
        return member.toDomain();
    }

    @Override
    public Member updateMemberInfo(Long memberId, String name, String school, Long studentId, String uploadImageObjectKey) {
        MemberEntity member = memberJpaRepository.findById(memberId).orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));
        member.updateProfile(name, school, studentId, uploadImageObjectKey);
        return member.toDomain();
    }

    @Override
    public Boolean checkDuplicateEmail(String toEmail) {
        return memberJpaRepository.findByEmail(toEmail).isPresent();
    }

    @Override
    public Page<Member> findAllByCondition(String school, Long studentId, String name, MemberRole role, Pageable pageable) {
        Page<MemberEntity> members = memberJpaRepository.findAllByCondition(
                school,
                studentId,
                name,
                role,
                pageable);
        return members.map(MemberEntity::toDomain);
    }

    @Override
    public Member updateMemberRole(Long id, MemberRole roleName) {
        MemberEntity member = memberJpaRepository.findById(id).orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));
        member.updateRole(roleName);
        return member.toDomain();
    }

}
