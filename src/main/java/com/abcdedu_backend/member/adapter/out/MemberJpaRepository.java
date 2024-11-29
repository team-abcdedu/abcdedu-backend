package com.abcdedu_backend.member.adapter.out;

import com.abcdedu_backend.member.adapter.out.entity.MemberEntity;
import com.abcdedu_backend.member.domain.MemberRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberJpaRepository extends JpaRepository<MemberEntity, Long> {
    Optional<MemberEntity> findByEmail(String mail);

    @Query("SELECT m FROM MemberEntity m " +
            "WHERE (:school IS NULL OR m.school LIKE %:school%) " +
            "AND (:studentId IS NULL OR m.studentId = :studentId) " +
            "AND (:name IS NULL OR m.name LIKE %:name%) " +
            "AND (:role IS NULL OR m.role = :role)")
//    @EntityGraph(attributePaths = {"comments", "posts"})
    Page<MemberEntity> findAllByCondition(@Param("school") String school,
                                    @Param("studentId") Long studentId,
                                    @Param("name") String name,
                                    @Param("role") MemberRole role,
                                    Pageable pageable);
}
