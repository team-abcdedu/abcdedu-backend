package com.abcdedu_backend.homework.dto;

import java.util.List;

public class HomeworkRes {
    public record MainModel(
        Long id,
        String title,
        String description
//        UserRes.Main teacher, TODO 필요시 추가

    ) {}

    public record DetailModel(
        Long id,
        String title,
        String description,
        List<SubjectQuestionModel> subjectQuestions,
        List<ShortQuestionModel> shortQuestions,
        List<MultipleOptionQuestionModel> multipleQuestions,
        List<SingleOptionQuestionModel> singleQuestions,
        String additionalDescription
    ){}


    public record SubjectQuestionModel(
        Long id,
        Integer index,
        String title,
        String description,
        Integer score

    ) {}

    public record ShortQuestionModel(
        Long id,
        Integer index,
        String title,
        String description,
        Integer score

    ) {}

    public record MultipleOptionQuestionModel(
        Long id,
        Integer index,
        String title,
        String description,
        Integer score,
        List<OptionModel> options
    ) {}

    public record SingleOptionQuestionModel(
        Long id,
        Integer index,
        String title,
        String description,
        Integer score,
        List<OptionModel> options
    ) {}

    public record OptionModel(
        String content,
        Integer index
    ) {}


}
