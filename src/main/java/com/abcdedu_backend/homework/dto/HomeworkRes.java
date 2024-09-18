package com.abcdedu_backend.homework.dto;

import com.abcdedu_backend.homework.entity.HomeworkQuestionType;

import java.util.List;

public class HomeworkRes {
    public record MainModel(
        Long id,
        String title,
        String subTitle
//        UserRes.Main teacher, TODO 필요시 추가

    ) {}

    public record DetailModel(
        Long id,
        String title,
        String subTitle,
        String description,
        List<QuestionModel> questions,
        String additionalDescription
    ){}


    public record QuestionModel(
        Long id,
        HomeworkQuestionType type,
        Integer index,
        String title,
        String description,
        Integer score,
        List<OptionModel> options

    ) {}

    public record OptionModel(
        Integer index,
        String content
    ) {}


}
