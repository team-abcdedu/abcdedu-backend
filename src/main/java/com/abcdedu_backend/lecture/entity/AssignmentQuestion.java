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
@Table(name = "assignmentQuestions")
public class AssignmentQuestion {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_id", nullable = false)
    private Assignment assignment;

    @Column(nullable = false, length = 100)
    private String body;

    @Column(nullable = false)
    private Integer orderNumber;

    @Enumerated(EnumType.STRING)
    private AssignmentAnswerType assignmentAnswerType;
}
