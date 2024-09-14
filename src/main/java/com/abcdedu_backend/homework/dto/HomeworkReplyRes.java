package com.abcdedu_backend.homework.dto;

import com.abcdedu_backend.homework.entity.HomeworkReply;

import java.util.List;

public class HomeworkReplyRes {
    public record UserReplyModel(
        Long questionId,
        String content,
        Long optionIndex,
        List<Long> optionIndexes
    ) {
        public static UserReplyModel from(HomeworkReply homeworkReply) {
            var payload = homeworkReply.getPayload();
            return new UserReplyModel(
                homeworkReply.getHomeworkQuestionId(),
                payload.getContent(),
                payload.getOptionIndex(),
                payload.getOptionIndexes()
            );
        }
    }

}
