package com.abcdedu_backend.homework.service;

import com.abcdedu_backend.homework.dto.request.HomeworkReplyReadReq;
import com.abcdedu_backend.homework.entity.Homework;
import com.abcdedu_backend.homework.entity.HomeworkQuestion;
import com.abcdedu_backend.homework.entity.HomeworkReply;
import com.abcdedu_backend.homework.repository.HomeworkReplyRepository;
import com.abcdedu_backend.member.entity.Member;
import com.abcdedu_backend.utils.exportable.ExcelData;
import com.abcdedu_backend.utils.exportable.Exportable;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class HomeworkReplyService {

    private final HomeworkReplyRepository replyRepository;
    private final Exportable exportable;

    public void exportRepliesByMember(HomeworkReplyReadReq req, HttpServletResponse response, Homework homework)  {
        // 데이터 조회
        List<HomeworkReply> replies = replyRepository.findRepliesByHomeworkAndCreatedAtBetween(homework, req.fromDate(), req.toDate());

        // 학생별로 질문과 응답을 매핑
        Map<Member, Map<HomeworkQuestion, String>> replyMap = replies.stream()
                .collect(Collectors.groupingBy(
                        HomeworkReply::getMember,
                        Collectors.toMap(
                                HomeworkReply::getHomeworkQuestion,
                                HomeworkReply::getAnswer
                        )
                ));

        // HomeworkQuestion 목록 추출
        List<HomeworkQuestion> questions = replies.stream()
                .map(HomeworkReply::getHomeworkQuestion)
                .distinct()
                .toList();

        // 엑셀 헤더 생성
        List<String> headerNames = new ArrayList<>();
        headerNames.add("Student Name");
        headerNames.addAll(questions.stream().map(HomeworkQuestion::getContent).toList());

        // 엑셀 행 데이터 생성
        List<List<Object>> rowData = new ArrayList<>();
        for (Map.Entry<Member, Map<HomeworkQuestion, String>> entry : replyMap.entrySet()) {
            List<Object> row = new ArrayList<>();
            row.add(entry.getKey().getName()); // 학생 이름
            for (HomeworkQuestion question : questions) {
                row.add(entry.getValue().getOrDefault(question, "")); // 질문별 응답 (없으면 빈 문자열)
            }
            rowData.add(row);
        }

        ExcelData excelData = new ExcelData("homework_replies", headerNames, rowData);
        exportable.export(response, excelData);
    }

}
