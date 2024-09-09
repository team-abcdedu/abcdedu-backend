package com.abcdedu_backend.survey.service;

import com.abcdedu_backend.exception.ApplicationException;
import com.abcdedu_backend.exception.ErrorCode;
import com.abcdedu_backend.member.entity.Member;
import com.abcdedu_backend.member.service.MemberService;
import com.abcdedu_backend.survey.dto.request.SurveyChoiceCreateRequest;
import com.abcdedu_backend.survey.dto.request.SurveyCreateRequest;
import com.abcdedu_backend.survey.dto.request.SurveyQuestionCreateRequest;
import com.abcdedu_backend.survey.dto.request.SurveyReplyCreateRequest;
import com.abcdedu_backend.survey.dto.response.*;
import com.abcdedu_backend.survey.entity.*;
import com.abcdedu_backend.survey.repository.SurveyQuestionChoiceRepository;
import com.abcdedu_backend.survey.repository.SurveyQuestionRepository;
import com.abcdedu_backend.survey.repository.SurveyReplyRepository;
import com.abcdedu_backend.survey.repository.SurveyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
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
                .member(member)
                .build();
        surveyRepository.save(survey);
        return survey.getId();
    }

    public List<SurveyListResponse> getSurveys(Long memberId) {
        checkSurveyPermmision(memberId);
        List<Survey> surveys = surveyRepository.findAll();
        return surveys.stream()
                .map(survey -> SurveyListResponse.builder()
                        .title(survey.getTitle())
                        .writerName(survey.getMember().getName())
                        .createAt(survey.getCreatedAt())
                        .build())
                .collect(Collectors.toUnmodifiableList());
    }

    public SurveyGetResponse getSurvey(Long memberId, Long surveyId) {
        checkSurveyPermmision(memberId);
        Survey findSurvey = checkSurvey(surveyId);
        return SurveyGetResponse.builder()
                .title(findSurvey.getTitle())
                .description(findSurvey.getDescription())
                .writerName(findSurvey.getMember().getName())
                .questionGetResponses(toSurveyQuestionGetResponse(findSurvey.getSurveyQuestions()))
                .choiseGetResponses(toSurveyQuestionChoiceGetResponse(findSurvey.getSurveyQuestions()))
                .build();
    }

    @Transactional
    public SurveyQuestion createQuestion(Long memberId, Long surveyId, SurveyQuestionCreateRequest request) {
        checkSurveyPermmision(memberId);
        Survey findSurvey = checkSurvey(surveyId);
        SurveyQuestion surveyQuestion = SurveyQuestion.builder()
                .type(SurveyQuestionType.fromDescription(request.type()))
                .isAnswerRequired(request.isAnswerRequired())
                .content(request.content())
                .survey(findSurvey)
                .build();
        findSurvey.getSurveyQuestions().add(surveyQuestion);
        questionRepository.save(surveyQuestion);
        return surveyQuestion;
    }

    @Transactional
    public void deleteSurvey(Long memberId, Long surveyId) {
        checkSurveyPermmision(memberId);
        Survey findSurvey = checkSurvey(surveyId);
        surveyRepository.delete(findSurvey);
    }

    @Transactional
    public List<SurveyQuestionChoice> createChoice(Long memberId, Long surveyId, Long questionId, List<SurveyChoiceCreateRequest> requests) {
        checkSurveyPermmision(memberId);
        Survey findSurvey = checkSurvey(surveyId);
        SurveyQuestion findQuestion = checkSurveyQuestion(questionId);
        List<SurveyQuestionChoice> choices = new ArrayList<>();
        for (SurveyChoiceCreateRequest request : requests) {
            SurveyQuestionChoice choice = SurveyQuestionChoice.builder()
                    .order(request.order())
                    .description(request.description())
                    .survey(findSurvey)
                    .surveyQuestion(findQuestion)
                    .build();
            choiceRepository.save(choice);
            choices.add(choice);
            findQuestion.getChoices().add(choice);
        }
        return choices;
    }

    public List<SurveyQuestionChoiceGetResponse> getchoices(Long surveyId, Long questionId) {
        Survey findSurvey = checkSurvey(surveyId);
        SurveyQuestion findQuestion = checkSurveyQuestion(questionId);
        if (findQuestion.getType() == SurveyQuestionType.ESSAY) {
            throw new ApplicationException(ErrorCode.SURVEY_QUESTION_CHOICE_IS_ESSAY);
        }
        List<SurveyQuestionChoice> choices = choiceRepository.findBySurveyIdAndSurveyQuestionId(surveyId, questionId);
        return choices.stream()
                .map(choice -> SurveyQuestionChoiceGetResponse.builder()
                        .order(choice.getOrder())
                        .description(choice.getDescription())
                        .build())
                .collect(Collectors.toUnmodifiableList());
    }


    // 응답 생성
    @Transactional
    public SurveyReply createSurveyReply(Long memberId, Long surveyId, Long questionId, SurveyReplyCreateRequest request) {
        Member replyedMember = checkSurveyCreatePermmision(memberId);
        Survey survey = checkSurvey(surveyId);
        SurveyQuestion surveyQuestion = checkSurveyQuestion(questionId);

        String integerToTextAnswer = request.answer();
        if (surveyQuestion.getType() == SurveyQuestionType.CHOICE) {
            integerToTextAnswer = choiceRepository.findBySurveyIdAndSurveyQuestionIdAndOrder(surveyId, questionId, Integer.parseInt(request.answer())).getDescription();
        }
        SurveyReply surveyReply = SurveyReply.builder()
                .survey(survey)
                .surveyQuestion(surveyQuestion)
                .member(replyedMember)
                .answer(integerToTextAnswer)
                .build();
        replyRepository.save(surveyReply);
        return surveyReply;
    }

    // 설문-질문-응답 조회
    public List<SurveyReplyGetResponse> getSurveyReply(Long memberId) {
        checkSurveyPermmision(memberId);
        List<SurveyReply> replies = replyRepository.findAll();
        return replies.stream()
                .map(reply -> SurveyReplyGetResponse.builder()
                                .surveyId(reply.getSurvey().getId())
                                .surveyTitle(reply.getSurvey().getTitle())
                                .questionId(reply.getSurveyQuestion().getId())
                                .questionDescription(reply.getSurveyQuestion().getContent())
                                .surveyReplyId(reply.getId())
                                .answer(reply.getAnswer())
                                .replyedMemberName(reply.getMember().getName())
                                .build())
                .collect(Collectors.toUnmodifiableList());

    }

    private Member checkSurveyPermmision(Long memberId) {
        Member findMember = memberService.checkMember(memberId);
        if (!findMember.isAdmin()) throw new ApplicationException(ErrorCode.ADMIN_VALID_PERMISSION);
        return findMember;
    }

    private Member checkSurveyCreatePermmision(Long memberId) {
        Member findMember = memberService.checkMember(memberId);
        if (!findMember.isStudent()) throw new ApplicationException(ErrorCode.STUDENT_VALID_PERMISSION);
        return findMember;
    }

    private Survey checkSurvey(Long surveyId) {
        return surveyRepository.findById(surveyId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.SURVEY_NOT_FOUND));
    }
    private SurveyQuestion checkSurveyQuestion(Long questionId) {
        return questionRepository.findById(questionId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.SURVEY_QUESTION_NOT_FOUND));
    }


    private List<SurveyQuestionGetResponse> toSurveyQuestionGetResponse(List<SurveyQuestion> surveyQuestions) {
        return surveyQuestions.stream()
                .map(surveyQuestion -> SurveyQuestionGetResponse.builder()
                        .type(surveyQuestion.getType().getDescription())
                        .isAnswerRequired(surveyQuestion.isAnswerRequired())
                        .content(surveyQuestion.getContent())
                        .build())
                .collect(Collectors.toUnmodifiableList());
    }

    private List<SurveyQuestionChoiceGetResponse> toSurveyQuestionChoiceGetResponse(List<SurveyQuestion> questions) {
        return questions.stream()
                .flatMap(question -> question.getChoices().stream())
                .map(choice -> SurveyQuestionChoiceGetResponse.builder()
                        .order(choice.getOrder())
                        .description(choice.getDescription())
                        .build())
                .collect(Collectors.toUnmodifiableList());
    }



}
