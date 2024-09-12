package com.abcdedu_backend.homework.dto;

import java.util.List;

public class HomeworkReplyReq {
    public record UpsertMany(
        List<UserReply> userReplies
    ) {}

    public record UserReply(
        Long questionId,
        String content,
        Long optionIndex,
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
                throw new IllegalArgumentException();
            }
        }
    }


}
