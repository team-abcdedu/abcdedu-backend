package com.abcdedu_backend.board;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BoardType {

    FREE("자유게시판"),
    QNA("Q & A"),
    PROJECT("ABCD Project"),
    DOCUMENT("자료실"),
    RATING("등업 게시판");

    private final String type;
}

