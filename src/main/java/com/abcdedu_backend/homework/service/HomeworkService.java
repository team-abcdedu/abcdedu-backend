package com.abcdedu_backend.homework.service;

import com.abcdedu_backend.exception.ApplicationException;
import com.abcdedu_backend.exception.ErrorCode;
import com.abcdedu_backend.homework.dto.response.HomeworkGetRes;
import com.abcdedu_backend.homework.dto.response.HomeworkQuestionGetRes;
import com.abcdedu_backend.homework.dto.request.HomeworkReplyCreateReq;
import com.abcdedu_backend.homework.dto.response.HomeworkReplyGetRes;
import com.abcdedu_backend.homework.entity.Homework;
import com.abcdedu_backend.homework.entity.HomeworkQuestion;
import com.abcdedu_backend.homework.entity.HomeworkReply;
import com.abcdedu_backend.homework.repository.HomeworkQuestionRepository;
import com.abcdedu_backend.homework.repository.HomeworkReplyRepository;
import com.abcdedu_backend.homework.repository.HomeworkRepository;
import com.abcdedu_backend.member.entity.Member;
import com.abcdedu_backend.member.entity.MemberRole;
import com.abcdedu_backend.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class HomeworkService {
    private final MemberService memberService;
    private final HomeworkRepository homeworkRepository;
    private final HomeworkQuestionRepository questionRepository;
    private final HomeworkReplyRepository replyRepository;

    public HomeworkGetRes getHomework(Long memberId, Long homeworkId) {
        checkPermission(memberId, MemberRole.STUDENT);
        Homework homework = checkHomework(homeworkId);
        return homeworkToHomeworkGetRes(homework);
    }

    @Transactional
    public void createHomeworkReply(Long memberId, Long homeworkId, List<HomeworkReplyCreateReq> replyRequests) {
        Member member = checkPermission(memberId, MemberRole.STUDENT);
        Homework homework = checkHomework(homeworkId);
        List<HomeworkQuestion> questions = checkQeustionsByHomework(homework);
        List<HomeworkReply> homeworkReplies = makeHomeworkReply(homework, questions, replyRequests, member);
        saveReplies(homeworkReplies);
    }

    public HomeworkReplyGetRes getReplies(Long memberId, Long homeworkId) {
        checkPermission(memberId, MemberRole.ADMIN);

        Homework homework = checkHomework(homeworkId);
        List<HomeworkQuestion> questions = checkQeustionsByHomework(homework);
        List<HomeworkReply> replies = checkRepliesByHomework(homework);

        // 설문 질문을 헤더로 설정
        List<String> questionHeaders = questions.stream()
                .map(HomeworkQuestion::getContent) // 질문 내용을 열 제목으로 사용
                .toList();

        // 각 사람의 응답을 레코드로 생성
        List<List<String>> records = new ArrayList<>();
        Map<Long, List<String>> respondentAnswersMap = new HashMap<>();

        for (HomeworkReply reply : replies) {
            Long respondentId = reply.getMember().getId(); // 응답자의 ID로 그룹화
            HomeworkQuestion relatedQuestion = reply.getHomeworkQuestion(); // 응답과 연관된 질문

            respondentAnswersMap
                    .computeIfAbsent(respondentId, k -> new ArrayList<>(Collections.nCopies(questions.size(), "")))  // 질문 수만큼 빈 값으로 초기화
                    .set(questions.indexOf(relatedQuestion), reply.getAnswer());  // 질문의 인덱스에 응답을 삽입
        }
        // 응답 데이터를 레코드 형식으로 변환
        records.addAll(respondentAnswersMap.values());

        log.info("응답 생성 성공, 응답 갯수 : {}", records.size());
        return new HomeworkReplyGetRes(questionHeaders, records);
    }

    public Homework checkHomework(Long homeworkId) {
        return homeworkRepository.findById(homeworkId).orElseThrow(() -> new ApplicationException(ErrorCode.HOMEWORK_NOT_FOUND));
    }

    public List<HomeworkQuestion> checkQeustionsByHomework(Homework homework) {
        return questionRepository.findAllByHomework(homework);
    }

    public List<HomeworkReply> checkRepliesByHomework(Homework homework) {
        return replyRepository.findAllByHomework(homework);
    }


    public Member checkPermission(Long memberId, MemberRole memberRole) {
        Member member = memberService.checkMember(memberId);
        if (memberRole == MemberRole.STUDENT) {
            if (!member.isStudent() && !member.isAdmin()) {
                log.warn("사용자 역할 권한 검증 실패");
                throw new ApplicationException(ErrorCode.STUDENT_VALID_PERMISSION);
            }
        } else if (memberRole == MemberRole.ADMIN) {
            log.warn("사용자 역할 권한 검증 실패");
            if (!member.isAdmin()) throw new ApplicationException(ErrorCode.ADMIN_VALID_PERMISSION);
        }
        log.info("checkPermission() : 권한 확인 성공");
        return member;
    }

    private void saveReplies(List<HomeworkReply> replies) {
        for (HomeworkReply reply : replies) {
            try {
                replyRepository.save(reply);
            } catch (Exception e) {
                log.error("saveReplies() : 데이터 베이스 저장 실패, message = {}", e.getMessage());
                throw new ApplicationException(ErrorCode.DATABASE_ERROR);
            }
        }
    }

    private List<HomeworkQuestionGetRes> HomeworkQuestionsToHomeworkQuestionGetRess(List<HomeworkQuestion> questions) {
        return questions.stream()
                .map(question -> HomeworkQuestionGetRes.builder()
                        .orderNumber(question.getOrderNumber())
                        .isAnswerRequired(question.isAnswerRequired())
                        .content(question.getContent())
                        .build())
                .toList();
    }

    private HomeworkGetRes homeworkToHomeworkGetRes(Homework homework) {
        return HomeworkGetRes.builder()
                .title(homework.getTitle())
                .description(homework.getDescription())
                .additionalDescription(homework.getAdditionalDescription())
                .questionGetResponses(HomeworkQuestionsToHomeworkQuestionGetRess(checkQeustionsByHomework(homework)))
                .build();
    }

    private List<HomeworkReply> makeHomeworkReply(Homework homework, List<HomeworkQuestion> questions, List<HomeworkReplyCreateReq> replyRequests, Member member) {
        List<HomeworkReply> replies = new ArrayList<>();
        for (int i = 0; i < questions.size(); i++) {
            replies.add(
                    HomeworkReply.builder()
                            .homework(homework)
                            .homeworkQuestion(questions.get(i))
                            .answer(replyRequests.get(i).answer())
                            .member(member)
                            .build()
            );
        }
        return replies;
    }


}
