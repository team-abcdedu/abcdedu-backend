package com.abcdedu_backend.homework.entity;

import com.abcdedu_backend.member.entity.Member;
import com.abcdedu_backend.utils.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
@Table(name = "homework_representatives")
public class HomeworkRepresentative extends BaseTimeEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "homework_id", nullable = false)
    private Homework homework;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member registrant; // 대표 공통과제로 등록한 관리자
}

