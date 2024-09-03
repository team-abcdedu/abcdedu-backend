package com.abcdedu_backend.lecture.entity;

import com.abcdedu_backend.member.entity.Member;
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
    @JoinColumn(name = "assignment_id", nullable = false)
    private Assignment assignment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_question_id", nullable = false)
    private AssignmentQuestion assignmentQuestion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false, length = 200)
    private String body;

//    @Column(nullable = false)
//    private Integer order;
//
//    @Enumerated(EnumType.STRING)
//    private AssignmentAnswerType assignmentAnswerType;
}
