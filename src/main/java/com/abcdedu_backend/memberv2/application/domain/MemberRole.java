package com.abcdedu_backend.memberv2.application.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberRole {
    ADMIN("관리자"),
    STUDENT("학생"),
    BASIC("새싹");

    private final String name;
}

