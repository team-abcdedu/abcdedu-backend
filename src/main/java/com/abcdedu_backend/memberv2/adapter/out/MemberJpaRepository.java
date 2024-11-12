package com.abcdedu_backend.memberv2.adapter.out;

import com.abcdedu_backend.memberv2.adapter.out.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberJpaRepository extends JpaRepository<MemberEntity, Long> {
    Optional<MemberEntity> findByEmail(String mail);
}
