package com.abcdedu_backend.homework.entity;

import com.abcdedu_backend.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class Homework {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column
    private String subTitle;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String additionalDescription;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "teacher_id", nullable = false)
    private Member teacher;

    @OneToMany(mappedBy = "homework", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HomeworkQuestion> questions = new ArrayList<>();


    public Long getTeacherId() {
        return teacher.getId();
    }

    public void update(HomeworkCommand.Update command) {
        this.title = command.getTitle();
        this.description = command.getDescription();
        this.additionalDescription = command.getAdditionalDescription();
    }


    public static Homework create(HomeworkCommand.Create command, Member teacher) {
        Homework homework = Homework.builder()
            .title(command.getTitle())
            .subTitle(command.getSubTitle())
            .description(command.getDescription())
            .additionalDescription(command.getAdditionalDescription())
            .teacher(teacher)
            .build();
        homework.questions = command
            .getCreateQuestionCommands().stream()
            .map(q -> HomeworkQuestion.create(homework, q))
            .toList();
        return homework;
    }

}
