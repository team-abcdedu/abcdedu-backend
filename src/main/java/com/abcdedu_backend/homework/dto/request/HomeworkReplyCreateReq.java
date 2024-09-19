package com.abcdedu_backend.homework.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

@Builder
public record HomeworkReplyCreateReq(
        @Schema(example = "저는 이 인공지능이 꼭 필요하다고 생각해요!", description = "1000자 이상 넘을 수 없습니다.")
        @Length(max = 1000)
        String answer
) {
}
