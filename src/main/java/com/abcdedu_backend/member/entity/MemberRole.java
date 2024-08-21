package com.abcdedu_backend.member.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public enum MemberRole {
    ADMIN,
    STUDENT,
    BASIC;
}
