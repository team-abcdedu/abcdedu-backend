package com.abcdedu_backend.homework.dto;

import com.abcdedu_backend.homework.entity.Homework;
import com.abcdedu_backend.homework.entity.HomeworkQuestion;
import com.abcdedu_backend.homework.entity.HomeworkQuestionType;

import java.util.List;

public class HomeworkRes {
    public record MainModel(
        Long id,
        String title,
        String subTitle
//        UserRes.Main teacher, TODO 필요시 추가

    ) {
        public static MainModel from(Homework homework) {
            return new MainModel(
                homework.getId(),
                homework.getTitle(),
                homework.getDescription()
            );
        }
    }

    public record DetailModel(
        Long id,
        String title,
        String subTitle,
        String description,
        List<QuestionModel> questions,
        String additionalDescription
    ) {
        public static DetailModel from(Homework homework) {
            return new DetailModel(
                homework.getId(),
                homework.getTitle(),
                homework.getSubTitle(),
                homework.getDescription(),
                homework.getQuestions().stream().map(QuestionModel::from).toList(),
                homework.getAdditionalDescription()
            );
        }
    }


    public record QuestionModel(
        Long id,
        HomeworkQuestionType type,
        Integer index,
        String title,
        String description,
        Integer score,
        List<OptionModel> options

    ) {
        public static QuestionModel from(HomeworkQuestion question) {
            List<OptionModel> options = null;
            if (question.getPayload() != null) {
                options = question.getPayload()
                    .getQuestionOptions().stream()
                    .map(OptionModel::from).toList();
            }
            return new QuestionModel(
                question.getId(),
                question.getType(),
                question.getIndex(),
                question.getTitle(),
                question.getDescription(),
                question.getScore(),
                options
            );
        }
    }

    public record OptionModel(
        Integer index,
        String content
    ) {
        public static OptionModel from(HomeworkQuestion.QuestionOption option) {
            return new OptionModel(
                option.getIndex(),
                option.getContent()
            );
        }
    }


}
