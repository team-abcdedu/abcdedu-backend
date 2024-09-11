package com.abcdedu_backend.lecture.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "assignmentAnswers")
public class AssignmentAnswer {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_submission_id", nullable = false)
    private AssignmentSubmission assignmentSubmission;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_question_id", nullable = false)
    private AssignmentQuestion assignmentQuestion;

    @Column(nullable = false, length = 200)
    private String body;

//    @Column(nullable = false)
//    private Integer order;
//
//    @Enumerated(EnumType.STRING)
//    private AssignmentAnswerType assignmentAnswerType;
}
