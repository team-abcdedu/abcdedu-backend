package com.abcdedu_backend.survey.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

import java.util.List;
@Builder
public record SurveyCreateRequest (
        @NotBlank
        @Schema(example = "수업 만족도 조사")
        @Length(max=50)
        String title,

        @Schema(example = """
                안녕하세요. "중고등학생을 위한 데이터 사이언스" 교육 서비스를 제공하는 ABCDEdu입니다.
                수업을 수강해주신 학생 분들께 진심으로 감사하다는 말씀 드립니다.
                                
                본 수업은 딥러닝을 구현하기 위해 수학이 어떻게 활용되는가에 대해 알아보는 프로그램으로, 인공지능과 관련된 일을 하기 위해 구체적으로 어떤 수학을 어떻게 공부해야 하는지에 대하여 탐구함으로써, 학교 수학을 공부하는 의미를 찾고 진로를 탐색하는 기회를 갖는 시간이었습니다.
                                
                본 수업의 발전을 위해 아래와 같은 설문 조사에 응해주시면 감사하겠습니다.
                """)
        @Length(max=300)
        String description,
        @Schema(example = """
                <h3>[문의]</h3>
                <p>이메일 : abcdedu@abcdedu.com</p>
                <p>인스타 : abcd_education</p>
                """)
        @Length(max=100)
        String additionalDescription,

        @NotNull
        List<@Valid SurveyQuestionCreateRequest> questions
){
}
