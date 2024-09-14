package com.abcdedu_backend.homework.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class HomeworkReplyCommand {
    @Builder
    @Getter
    public static class Upsert {
        private final Long questionId;
        private final String content;
        private final Long optionIndex;
        private final List<Long> optionIndexes;
    }
}
