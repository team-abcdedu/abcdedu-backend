package com.abcdedu_backend.homework.service;

import com.abcdedu_backend.exception.ApplicationException;
import com.abcdedu_backend.exception.ErrorCode;
import com.abcdedu_backend.homework.dto.response.HomeworkGetRes;
import com.abcdedu_backend.homework.dto.response.HomeworkQuestionGetRes;
import com.abcdedu_backend.homework.dto.request.HomeworkReplyCreateReq;
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

    public Homework checkHomework(Long homeworkId) {
        return homeworkRepository.findById(homeworkId).orElseThrow(() -> new ApplicationException(ErrorCode.HOMEWORK_NOT_FOUND));
    }

    public List<HomeworkQuestion> checkQeustionsByHomework(Homework homework) {
        return questionRepository.findAllByHomework(homework);
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
                        .additionalContent(question.getAdditionalContent())
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
