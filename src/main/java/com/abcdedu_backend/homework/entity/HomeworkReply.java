package com.abcdedu_backend.homework.entity;

import com.abcdedu_backend.member.entity.Member;
import com.abcdedu_backend.utils.BaseTimeEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
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
public class HomeworkReply extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "homework_question_id", nullable = false)
    private HomeworkQuestion homeworkQuestion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(columnDefinition = "JSON")
    @JdbcTypeCode(SqlTypes.JSON)
    private ReplyPayload payload;

    public Long getHomeworkQuestionId() {
        return this.homeworkQuestion.getId();
    }

    public void update(HomeworkReplyCommand.Upsert command) {
        this.payload = ReplyPayload.create(command);
    }

    public static HomeworkReply create(
        Member member,
        HomeworkQuestion homeworkQuestion,
        HomeworkReplyCommand.Upsert command
    ) {
        ReplyPayload payload = ReplyPayload.create(command);

        return HomeworkReply.builder()
            .member(member)
            .homeworkQuestion(homeworkQuestion)
            .payload(payload)
            .build();
    }

    @Builder
    @Getter
    @EqualsAndHashCode
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ReplyPayload {
        private final String content;
        private final Long optionIndex;
        private final List<Long> optionIndexes;

        public static ReplyPayload create(HomeworkReplyCommand.Upsert command) {
            return ReplyPayload.builder()
                .content(command.getContent())
                .optionIndex(command.getOptionIndex())
                .optionIndexes(command.getOptionIndexes())
                .build();
        }
    }
}
