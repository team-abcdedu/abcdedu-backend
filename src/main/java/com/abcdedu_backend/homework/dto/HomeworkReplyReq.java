package com.abcdedu_backend.homework.dto;

import com.abcdedu_backend.exception.ApplicationException;
import com.abcdedu_backend.exception.ErrorCode;
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
            int notNullCount = 0;
            if (content != null) {
                notNullCount++;
            }
            if (optionIndex != null) {
                notNullCount++;
            }
            if (optionIndexes != null) {
                notNullCount++;
            }
            if (notNullCount != 1) {
                throw new ApplicationException(ErrorCode.INVALID_REQUEST);
            }
        }
    }


}
