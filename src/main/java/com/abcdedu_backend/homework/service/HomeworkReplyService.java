package com.abcdedu_backend.homework.service;

import com.abcdedu_backend.homework.entity.HomeworkQuestion;
import com.abcdedu_backend.homework.entity.HomeworkReply;
import com.abcdedu_backend.homework.entity.HomeworkReplyCommand;
import com.abcdedu_backend.homework.repository.HomeworkQuestionRepository;
import com.abcdedu_backend.homework.repository.HomeworkReplyRepository;
import com.abcdedu_backend.member.entity.Member;
import com.abcdedu_backend.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class HomeworkReplyService {
    private final MemberRepository memberRepository;
    private final HomeworkQuestionRepository homeworkQuestionRepository;
    private final HomeworkReplyRepository homeworkReplyRepository;

    /**
     * 응답 생성 Upsert
     * 1. [HomeworkQuestion]을 모두 조회하여 영속성 컨텍스트에 로드
     * 2. 유저의 기존 응답을 모두 조회
     * 3. 기존 응답이 존재하면 command로 업데이트
     * 4. 기존 응답이 없는 command는 새로 생성하여 저장
     */
    @Transactional
    public void upsertHomeworkReply(
        Long homeworkId,
        Long memberId,
        List<HomeworkReplyCommand.Upsert> commands
    ) {
        // 1. 멤버 프록시 조회
        Member member = memberRepository.getReferenceById(memberId);

        // 2. 요청에 해당하는 질문을 모두 조회하고 Map으로 변환 <질문 ID, 질문>
        List<Long> questionIds = commands
            .stream()
            .map(HomeworkReplyCommand.Upsert::getQuestionId)
            .toList();

        // 질문 ID에 해당하는 질문을 모두 조회하여 영속성 컨텍스트에 로드
        List<HomeworkQuestion> homeworkQuestions = homeworkQuestionRepository.findAllById(questionIds);

        Map<Long, HomeworkQuestion> homeworkQuestionMap = homeworkQuestions
            .stream()
            .filter(hq -> hq.getHomeworkId().equals(homeworkId)) // homeworkId에 해당하는 질문만 필터링(방어로직)
            .collect(Collectors.toMap(HomeworkQuestion::getId, Function.identity()));

        if (commands.size() != homeworkQuestionMap.size()) {
            throw new IllegalArgumentException("과제에서 질문 ID에 해당하는 질문이 존재하지 않습니다.");
        }

        // 3. 기존의 응답을 모두 조회하고 Map으로 변환 <질문 ID, 응답>
        Map<Long, HomeworkReply> existedRepliesMap = homeworkReplyRepository
            .findAllByMemberIdAndQuestionIdsIn(memberId, questionIds)
            .stream()
            .collect(Collectors.toMap(HomeworkReply::getHomeworkQuestionId, Function.identity()));

        // 4. 커맨드를 처리하여 응답 업데이트 또는 생성
        List<HomeworkReply> repliesToSave = doUpsert(commands, existedRepliesMap, homeworkQuestionMap, member);

        // 5. 저장 (업데이트된 응답과 새로 생성된 응답 모두 처리)
        homeworkReplyRepository.saveAll(repliesToSave);
    }

    private List<HomeworkReply> doUpsert(
        List<HomeworkReplyCommand.Upsert> commands,
        Map<Long, HomeworkReply> existedRepliesMap,
        Map<Long, HomeworkQuestion> homeworkQuestionMap,
        Member member
    ) {
        return commands.stream().map(command -> {
            Long questionId = command.getQuestionId();
            HomeworkReply existingReply = existedRepliesMap.get(questionId);

            if (existingReply != null) {
                // 기존 응답이 있으면 업데이트
                existingReply.update(command);
                return existingReply;
            } else {
                // 새 응답 생성
                HomeworkQuestion homeworkQuestion = homeworkQuestionMap.get(questionId);
                return HomeworkReply.create(member, homeworkQuestion, command);
            }
        }).toList();
    }
}
