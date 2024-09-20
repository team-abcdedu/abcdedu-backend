package com.abcdedu_backend.survey.service;

import com.abcdedu_backend.exception.ApplicationException;
import com.abcdedu_backend.exception.ErrorCode;
import com.abcdedu_backend.member.entity.Member;
import com.abcdedu_backend.member.service.MemberService;
import com.abcdedu_backend.survey.dto.request.SurveyCreateRequest;
import com.abcdedu_backend.survey.dto.request.SurveyReplyCreateRequest;
import com.abcdedu_backend.survey.dto.response.*;
import com.abcdedu_backend.survey.entity.*;
import com.abcdedu_backend.survey.repository.SurveyQuestionChoiceRepository;
import com.abcdedu_backend.survey.repository.SurveyQuestionRepository;
import com.abcdedu_backend.survey.repository.SurveyReplyRepository;
import com.abcdedu_backend.survey.repository.SurveyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SurveyService {
    private final SurveyRepository surveyRepository;
    private final SurveyQuestionRepository questionRepository;
    private final SurveyQuestionChoiceRepository choiceRepository;
    private final SurveyReplyRepository replyRepository;
    private final MemberService memberService;

    @Transactional
    public Long createSurvey(SurveyCreateRequest request, Long memberId) {
        Member member = checkSurveyPermmision(memberId);

        Survey survey = Survey.builder()
                .title(request.title())
                .description(request.description())
                .additionalDescription(request.additionalDescription())
                .member(member)
                .build();
        surveyRepository.save(survey);

        request.questions().forEach(reqQuestion -> {

            SurveyQuestion surveyQuestion = SurveyQuestion.builder()
                    .type(reqQuestion.type())
                    .orderNumber(reqQuestion.orderNumber())
                    .content(reqQuestion.content())
                    .isAnswerRequired(reqQuestion.isAnswerRequired())
                    .survey(survey)
                    .build();
            questionRepository.save(surveyQuestion);
            // 서술형은 선택지가 null이거나 비어있을 수 있다.
            if (reqQuestion.choices() != null && !(reqQuestion.choices().isEmpty())) {
                reqQuestion.choices().forEach(reqChoice -> {
                    SurveyQuestionChoice choice = SurveyQuestionChoice.builder()
                            .orderNumber(reqChoice.orderNumber())
                            .description(reqChoice.description())
                            .surveyQuestion(surveyQuestion)
                            .build();
                    choiceRepository.save(choice);
                });
            }
        });

        return survey.getId();
    }

    /**
     * 설문지 목록 조회
     */
    public Page<SurveyListResponse> getSurveys(Long memberId, Pageable pageable) {
        checkSurveyPermmision(memberId);
        Page<Survey> surveys = surveyRepository.findAll(pageable);
        return surveys
                .map(survey -> SurveyListResponse.builder()
                        .id(survey.getId())
                        .title(survey.getTitle())
                        .writerName(survey.getMember().getName())
                        .createAt(survey.getCreatedAt())
                        .build());
    }

    /**
     * 선택한 설문지로 응답을 하기 위해 설문지를 조회한다. (질문-선택지 포함)
     */
    public SurveyGetResponse getSurvey(Long memberId, Long surveyId) {
        checkSurveyPermmision(memberId);
        Survey findSurvey = checkSurvey(surveyId);
        List<SurveyQuestion> findQuestions = questionRepository.findBySurvey(findSurvey);

        return SurveyGetResponse.builder()
                .title(findSurvey.getTitle())
                .description(findSurvey.getDescription())
                .writerName(findSurvey.getMember().getName())
                .questionGetResponses(findQuestions.stream()
                        .map(question -> {
                            // 해당 질문과 관련된 선택지 필터링
                            List<SurveyQuestionChoiceGetResponse> relatedChoices = choiceRepository.findBysurveyQuestion(question).stream()
                                    .filter(choice -> choice.getSurveyQuestion().getId().equals(question.getId()))  // 선택지가 해당 질문에 속하는지 확인
                                    .map(choice -> SurveyQuestionChoiceGetResponse.builder()
                                            .orderNumber(choice.getOrderNumber())  // 선택지 순서
                                            .description(choice.getDescription())  // 선택지 설명
                                            .build())
                                    .collect(Collectors.toList());

                            // 질문 응답 빌더
                            return SurveyQuestionGetResponse.builder()
                                    .type(question.getType())  // 질문 타입
                                    .orderNumber(question.getOrderNumber())  // 질문 순서
                                    .isAnswerRequired(question.isAnswerRequired())  // 답변 필수 여부
                                    .content(question.getContent())  // 질문 내용
                                    .choices(relatedChoices)  // 관련된 선택지 리스트
                                    .build();
                        })
                        .collect(Collectors.toList()))  // 질문 리스트로 수집
                .build();
    }


    @Transactional
    public void deleteSurvey(Long memberId, Long surveyId) {
        checkSurveyPermmision(memberId);
        Survey findSurvey = checkSurvey(surveyId);
        surveyRepository.delete(findSurvey);
    }


    // 응답 생성
    @Transactional
    public void createSurveyReply(Long memberId, Long surveyId, List<SurveyReplyCreateRequest> replysRequests) {
        Member replyedMember = checkSurveyCreatePermmision(memberId);
        Survey findSurvey = checkSurvey(surveyId);
        List<SurveyQuestion> findQuestions = questionRepository.findBySurvey(findSurvey);

        int requestIdx = 0;
        for (SurveyReplyCreateRequest replyReq : replysRequests) {
            String translatedAnswer = replyReq.answer();
            SurveyQuestion getSurveyQuestion = findQuestions.get(requestIdx++);
            if (replyReq.type() == SurveyQuestionType.CHOICE) {
                // 선택지 필터링: 해당 질문과 관련된 선택지 중 선택된 답변과 매칭되는 것을 찾음
                List<SurveyQuestionChoice> findChoices = choiceRepository.findBysurveyQuestion(getSurveyQuestion);
                Optional<SurveyQuestionChoice> selectedChoice = findChoices.stream()
                        .filter(choice -> choice.getSurveyQuestion().getId().equals(getSurveyQuestion.getId()))  // 해당 질문의 선택지인지 확인
                        .filter(choice -> choice.getOrderNumber().equals(Integer.parseInt(replyReq.answer())))  // 선택지 순서 비교
                        .findFirst();
                if (!selectedChoice.isPresent()) {
                    throw new ApplicationException(ErrorCode.SURVEY_CHOICE_NUMBER_NOT_FOUND);
                }
                translatedAnswer = selectedChoice.get().getDescription();
            }

            SurveyReply surveyReply = SurveyReply.builder()
                    .survey(findSurvey)
                    .surveyQuestion(getSurveyQuestion)
                    .member(replyedMember)
                    .answer(translatedAnswer)
                    .build();
            replyRepository.save(surveyReply);
        }
    }


    // 질문-응답 조회
    public SurveyRepliesGetResponse getSurveyReplies(Long memberId, Long surveyId) {
        //checkSurveyPermmision(memberId);
        Survey findSurvey = checkSurvey(surveyId);
        List<SurveyQuestion> questions = questionRepository.findBySurvey(findSurvey);
        List<SurveyReply> replies = replyRepository.findBySurvey(findSurvey);

        // 설문 질문을 헤더로 설정
        List<String> questionHeaders = questions.stream()
                .map(SurveyQuestion::getContent)  // 질문 내용을 열 제목으로 사용
                .collect(Collectors.toList());

        // 각 사람의 응답을 레코드로 생성
        List<List<String>> records = new ArrayList<>();
        Map<Long, List<String>> respondentAnswersMap = new HashMap<>();

        for (SurveyReply reply : replies) {
            Long respondentId = reply.getMember().getId();  // 응답자의 ID로 그룹화
            SurveyQuestion relatedQuestion = reply.getSurveyQuestion();  // 응답과 연관된 질문

            respondentAnswersMap
                    .computeIfAbsent(respondentId, k -> new ArrayList<>(Collections.nCopies(questions.size(), "")))  // 질문 수만큼 빈 값으로 초기화
                    .set(questions.indexOf(relatedQuestion), reply.getAnswer());  // 질문의 인덱스에 응답을 삽입
        }

        // 응답 데이터를 레코드 형식으로 변환
        records.addAll(respondentAnswersMap.values());

        return new SurveyRepliesGetResponse(questionHeaders, records);
    }

    private Member checkSurveyPermmision(Long memberId) {
        Member findMember = memberService.checkMember(memberId);
        if (!findMember.isAdmin()) throw new ApplicationException(ErrorCode.ADMIN_VALID_PERMISSION);
        return findMember;
    }

    private Member checkSurveyCreatePermmision(Long memberId) {
        Member findMember = memberService.checkMember(memberId);
        if (!findMember.isStudent() && !findMember.isAdmin())
            throw new ApplicationException(ErrorCode.STUDENT_VALID_PERMISSION);
        return findMember;
    }

    private Survey checkSurvey(Long surveyId) {
        return surveyRepository.findById(surveyId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.SURVEY_NOT_FOUND));
    }

}
