package com.abcdedu_backend.member.repository;

import com.abcdedu_backend.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByEmail(String mail);
}
