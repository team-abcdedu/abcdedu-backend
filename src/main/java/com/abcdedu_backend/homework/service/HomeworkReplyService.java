package com.abcdedu_backend.homework.service;

import com.abcdedu_backend.homework.entity.HomeworkQuestion;
import com.abcdedu_backend.homework.entity.HomeworkReply;
import com.abcdedu_backend.homework.repository.HomeworkReplyRepository;
import com.abcdedu_backend.member.entity.Member;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class HomeworkReplyService {

    private final HomeworkReplyRepository replyRepository;

    public void exportRepliesByMember(HttpServletResponse response, Long homeworkId) throws IOException {
        // 데이터 조회
        List<HomeworkReply> replies = replyRepository.findRepliesByHomeworkId(homeworkId);
        // 학생별로 질문과 응답을 매핑
        Map<Member, Map<HomeworkQuestion, String>> replyMap = replies.stream()
                .collect(Collectors.groupingBy(
                        HomeworkReply::getMember,
                        Collectors.toMap(
                                HomeworkReply::getHomeworkQuestion,
                                HomeworkReply::getAnswer
                        )
                ));

        // 엑셀 생성
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("homework replies");

            // 헤더 생성
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("student name");
            // HomeworkQustion 목록 추출 및 열 헤더로 사용
            List<HomeworkQuestion> questions = replies.stream()
                    .map(HomeworkReply::getHomeworkQuestion)
                    .distinct()
                    .toList();
            for (int i=0; i < questions.size(); i++) {
                headerRow.createCell(i+1).setCellValue(questions.get(i).getContent());
            }

            // 학생별 데이터 작성
            int rowIndex = 1;
            for (Map.Entry<Member, Map<HomeworkQuestion, String>> entry : replyMap.entrySet()) {
                Row row = sheet.createRow(rowIndex++);
                Member member = entry.getKey();
                Map<HomeworkQuestion, String> questionReplies = entry.getValue();

                // 첫번째 열은 학생 이름
                row.createCell(0).setCellValue(member.getName());
                // 질문별 응답 입력
                for (int i=0; i<questions.size(); i++) {
                    HomeworkQuestion question = questions.get(i);
                    String replyContent = questionReplies.getOrDefault(question, ""); // 응답이 없으면 빈 문자열
                    row.createCell(i+1).setCellValue(replyContent);
                }
            }
            // 자동 열 너비 조정
            for (int i = 0; i <= questions.size(); i++) {
                sheet.autoSizeColumn(i);
            }
            // 파일 출력 설정
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment;filename=homework_replies.xlsx");

            workbook.write(response.getOutputStream());
        }

    }


}
