package com.abcdedu_backend.homework.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class HomeworkQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "homework_id", nullable = false)
    private Homework homework;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private HomeworkQuestionType type;

    @Column(nullable = false)
    private Integer index;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Integer score;

    @Column(columnDefinition = "JSON")
    @JdbcTypeCode(SqlTypes.JSON)
    private QuestionPayload payload;

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class QuestionPayload {
        private List<QuestionOption> questionOptions;

    }
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class QuestionOption {
        private Integer index;
        private String content;

        public static QuestionOption create(HomeworkQuestionCommand.CreateOption command) {
            return QuestionOption.builder()
                .index(command.getIndex())
                .content(command.getContent())
                .build();
        }
    }

    public Long getHomeworkId() {
        return this.homework.getId();
    }

    public static HomeworkQuestion create(Homework homework, HomeworkQuestionCommand.Create command) {
        QuestionPayload payload = null;
        if (command.getCreateOptionsCommand() != null) {
            payload = new QuestionPayload(command.getCreateOptionsCommand().stream()
                .map(QuestionOption::create)
                .toList());
        }
        return HomeworkQuestion.builder()
            .homework(homework)
            .type(command.getType())
            .index(command.getIndex())
            .title(command.getTitle())
            .description(command.getDescription())
            .score(command.getScore())
            .payload(payload)
            .build();
    }
}
