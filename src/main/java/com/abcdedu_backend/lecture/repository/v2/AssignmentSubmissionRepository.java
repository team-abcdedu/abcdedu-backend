package com.abcdedu_backend.lecture.repository.v2;

import com.abcdedu_backend.lecture.entity.v2.AssignmentSubmission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AssignmentSubmissionRepository extends JpaRepository<AssignmentSubmission, Long> {
    @EntityGraph(attributePaths = {"member", "assignment"})
    @Query("SELECT DISTINCT a FROM AssignmentSubmission a")
    Page<AssignmentSubmission> findAllWithMemberAndAssignmentAndSubLecture(Pageable pageable);
}
