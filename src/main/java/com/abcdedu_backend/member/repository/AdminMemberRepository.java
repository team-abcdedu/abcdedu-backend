package com.abcdedu_backend.member.repository;

import com.abcdedu_backend.member.entity.Member;
import com.abcdedu_backend.member.entity.MemberRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AdminMemberRepository extends JpaRepository<Member, Long> {
    @Query("SELECT m FROM Member m " +
            "WHERE (:school IS NULL OR m.school LIKE %:school%) " +
            "AND (:studentId IS NULL OR m.studentId = :studentId) " +
            "AND (:name IS NULL OR m.name LIKE %:name%) " +
            "AND (:role IS NULL OR m.role = :role)")
    Page<Member> findAllByCondition(@Param("school") String school,
                                    @Param("studentId") Long studentId,
                                    @Param("name") String name,
                                    @Param("role") MemberRole role,
                                    Pageable pageable);
}
