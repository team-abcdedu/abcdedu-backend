package com.abcdedu_backend.homework.dto;

import java.util.List;

public class HomeworkReplyRes {
    public record UserReplyModel(
        Long questionId,
        String content,
        Long optionIndex,
        List<Long> optionIndexes
    ) {
    }

}
