package com.abcdedu_backend.homework.service;

import com.abcdedu_backend.exception.ApplicationException;
import com.abcdedu_backend.exception.ErrorCode;
import com.abcdedu_backend.homework.dto.request.HomeworkReplyReadReq;
import com.abcdedu_backend.homework.dto.response.ReplyWithMemberResponse;
import com.abcdedu_backend.homework.entity.Homework;
import com.abcdedu_backend.homework.entity.HomeworkQuestion;
import com.abcdedu_backend.homework.repository.HomeworkReplyRepository;
import com.abcdedu_backend.member.entity.Member;
import com.abcdedu_backend.utils.exportable.ExcelData;
import com.abcdedu_backend.utils.exportable.Exportable;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class HomeworkReplyService {

    private final HomeworkReplyRepository replyRepository;
    private final Exportable exportable;

    public void exportRepliesByMember(HomeworkReplyReadReq req, HttpServletResponse response, Homework homework) {
        List<ReplyWithMemberResponse> replies = new ArrayList<>();
        try {
            replies = replyRepository.findRepliesByHomeworkAndCreatedAtBetween(homework, req.fromDate(), req.toDate());
        } catch (DataAccessException e) {
            log.error("replyRepository.findRepliesByHomeworkAndCreatedAtBetween() 실패 ==================");
            throw new ApplicationException(ErrorCode.DATABASE_ERROR);
        }
        // HomeworkQuestion 목록 추출
        List<HomeworkQuestion> questions = replies.stream()
                .map(reply -> reply.getHomeworkReply().getHomeworkQuestion())
                .distinct()
                .toList();

        ExcelData excelData = new ExcelData("homework_replies", generateHeader(questions), generateRow(replies, questions));
        exportable.export(response, excelData);
    }

    private List<String> generateHeader(List<HomeworkQuestion> questions) {
        List<String> headerNames = new ArrayList<>();
        headerNames.add("School Name");
        headerNames.add("Student ID");
        headerNames.add("Student Name");
        headerNames.addAll(questions.stream().map(HomeworkQuestion::getContent).toList());
        return headerNames;
    }

    private List<List<Object>> generateRow(List<ReplyWithMemberResponse> replies, List<HomeworkQuestion> questions) {
        Map<Long, Map<HomeworkQuestion, String>> replyMap = new HashMap<>();
        try {
            // 학생별로 질문과 응답을 매핑
            replyMap = replies.stream()
                    .collect(Collectors.groupingBy(
                            reply -> reply.getMember().getId(), // 사용자 id를 기준으로 그룹화
                            Collectors.toMap(
                                    reply -> reply.getHomeworkReply().getHomeworkQuestion(),
                                    reply -> reply.getHomeworkReply().getAnswer()
                            )
                    ));
        } catch (Exception e) {
            log.error("HomeworkReplyService.generateRow : mapping");
            throw new ApplicationException(ErrorCode.EXPORT_MAPPING_ERROR);
        }


        List<List<Object>> rowData = new ArrayList<>();
        try {
            for (ReplyWithMemberResponse reply : replies) {
                Member member = reply.getMember();

                Map<HomeworkQuestion, String> studentReplies = replyMap.get(member.getId());
                List<Object> row = new ArrayList<>();

                row.add(member.getSchool() != null ? member.getSchool() : ""); // 학교명
                row.add(member.getStudentId() != null ? member.getStudentId() : "");  // 학번
                row.add(member.getName());       // 학생 이름

                for (HomeworkQuestion question : questions) {
                    row.add(studentReplies != null ? studentReplies.getOrDefault(question, "") : "");
                }
                rowData.add(row);
            }
        } catch (Exception e) {
            log.error("HomeworkReplyService.generateRow");
            throw new ApplicationException(ErrorCode.EXPORT_IO_ERROR);
        }

        return rowData;
    }
}
