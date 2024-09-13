package com.abcdedu_backend.homework.service;

import com.abcdedu_backend.common.response.PagedResponse;
import com.abcdedu_backend.homework.dto.HomeworkRes;
import com.abcdedu_backend.homework.entity.Homework;
import com.abcdedu_backend.homework.repository.HomeworkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HomeworkQueryService {
    private final HomeworkRepository homeworkRepository;

    @Transactional(readOnly = true)
    public PagedResponse<HomeworkRes.MainModel> getHomeworksPaging(PageRequest pageRequest) {
        Page<HomeworkRes.MainModel> homeworks = homeworkRepository
            .findAll(pageRequest)
            .map(HomeworkRes.MainModel::from);
        return PagedResponse.from(homeworks);
    }

    @Transactional(readOnly = true)
    public HomeworkRes.DetailModel getHomeworkDetail(Long homeworkId) {
        Homework homework = homeworkRepository.findById(homeworkId).orElseThrow();
        return HomeworkRes.DetailModel.from(homework);
    }
}
