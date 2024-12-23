package com.abcdedu_backend.homework.service;

import com.abcdedu_backend.exception.ApplicationException;
import com.abcdedu_backend.exception.ErrorCode;
import com.abcdedu_backend.homework.entity.Homework;
import com.abcdedu_backend.homework.entity.HomeworkRepresentative;
import com.abcdedu_backend.homework.repository.HomeworkRepresentativeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class ReadRepresentativeService {

    private final HomeworkRepresentativeRepository representativeRepository;

    public Long readRepresentativeHomework() {
        Homework homework = representativeRepository.findLatestRepresentative()
                .map(HomeworkRepresentative::getHomework)
                .orElseThrow(() -> new ApplicationException(ErrorCode.REPRESENTATIVE_HOMEWORK_NOT_FOUND));
        return homework.getId();
    }

}
