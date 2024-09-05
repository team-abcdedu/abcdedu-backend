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
@Table(name = "assignmentFiles")
public class AssignmentFile {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_lecture_id", nullable = false)
    private SubLecture subLecture;

    @Enumerated(EnumType.STRING)
    private AssignmentType assignmentType;

    @OneToOne(mappedBy = "assignmentFile")
    private AssignmentAnswerFile assignmentAnswerFile;

    @Column(length=100)
    private String objectKey;

}
