package com.abcdedu_backend.memberv2.adapter.out;

import com.abcdedu_backend.memberv2.adapter.out.entity.MemberEntity;
import com.abcdedu_backend.memberv2.application.domain.Member;
import com.abcdedu_backend.memberv2.application.out.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@AllArgsConstructor
@Repository
public class MemberPersistence implements MemberRepository {

    private final MemberJpaRepository memberJpaRepository;

    @Override
    public void save(Member member) {
        memberJpaRepository.save(MemberEntity.from(member));
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        return memberJpaRepository.findByEmail(email).map(MemberEntity::toDomain);
    }

    @Override
    public Optional<Member> findById(Long memberId) {
        return memberJpaRepository.findById(memberId).map(MemberEntity::toDomain);
    }
}
