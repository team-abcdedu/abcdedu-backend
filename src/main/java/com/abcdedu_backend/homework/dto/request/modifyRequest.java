package com.abcdedu_backend.homework.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record modifyRequest(
        @Schema(example = "사회 문제 해결을 위한 인공지능 설계")
        @NotBlank(message = "값이 비었습니다.")
        String title,

        @Schema(example = "다양한 사례를 통해 인공지능이 사회문제를 해결하기 위해 " +
                "어떻게 사용되는지 알아보자. 그리고 다음 과제를 수행해보자.")
        String description,

        @Schema(example = "[인공지능으로 사회문제를 해결한 사례 시청]")
        String additionalDescription,

        @NotNull(message = "값이 비었습니다.")
        @Schema(example = "1L 와 같이 과제를 제작하는 로그인된 관리자의 id를 넣어주세요")
        Long memberId
) {

}
