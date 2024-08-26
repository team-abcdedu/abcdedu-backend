package com.abcdedu_backend.member.entity;

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
