package com.abcdedu_backend.contact.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ContactType {
    CLASS("강의"),
    TRAINING("교사연수"),
    ETC("기타");

    private final String name;
}
