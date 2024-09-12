package com.abcdedu_backend.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class HomeworkReplyReq {
    public record UpsertMany(
        List<UserReply> userReplies
    ) {}

    public record UserReply(
        Long questionId,
        @Schema(example = "인공지능과 관련된 기술을 사용하여 사회 문제를 해결하고 싶습니다.")
        String content,
        @Schema(example = "null")
        Long optionIndex,
        @Schema(example = "null")
        List<Long> optionIndexes
    ) {
        public UserReply {
            int nullCount = 0;
            if (optionIndex() == null) {
                nullCount++;
            }
            if (optionIndexes() == null) {
                nullCount++;
            }
            if (content() == null) {
                nullCount++;
            }
            if (nullCount != 1) {
                //TODO
                throw new IllegalArgumentException();
            }
        }
    }


}
