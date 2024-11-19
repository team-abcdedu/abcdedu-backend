package com.abcdedu_backend.memberv2.application;

import com.abcdedu_backend.exception.ApplicationException;
import com.abcdedu_backend.exception.ErrorCode;
import com.abcdedu_backend.memberv2.adapter.out.MemberJpaRepository;
import com.abcdedu_backend.memberv2.adapter.out.entity.MemberEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Transactional(readOnly = true)
@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {

    private final MemberJpaRepository memberV1Repository;

    public MemberEntity checkMember(Long memberId) {
        return memberV1Repository.findById(memberId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));
    }

}
