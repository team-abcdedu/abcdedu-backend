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
@Table(name = "subLectures")
public class SubLecture {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id", nullable = false)
    private Lecture lecture;

    @Column(nullable = false, length = 20)
    private String title;

    @Column(nullable = false)
    private Integer orderNumber;

    @Column(nullable = false, length = 200)
    private String description;

    @Column(nullable = false, length = 1)
    private String type;
}
