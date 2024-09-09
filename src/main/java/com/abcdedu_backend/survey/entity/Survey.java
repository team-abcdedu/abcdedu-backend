package com.abcdedu_backend.survey.entity;

import com.abcdedu_backend.member.entity.Member;
import com.abcdedu_backend.utils.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
@Table(name = "surveys")
public class Survey extends BaseTimeEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false, length = 20)
    private String title;

    @Column(length = 100)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member; // 설문을 만든 작성자, *관리자만 가능하다.*

    @OneToMany(mappedBy = "survey")
    private List<SurveyQuestion> surveyQuestions;

    public void updateSurvey(String title, String description) {
        this.title = title;
        this.description = description;
    }
}
