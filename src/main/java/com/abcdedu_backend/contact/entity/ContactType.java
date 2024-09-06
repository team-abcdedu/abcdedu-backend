package com.abcdedu_backend.contact.entity;

import com.abcdedu_backend.exception.ApplicationException;
import com.abcdedu_backend.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public enum ContactType {
    TRAINING("training"),
    CLASS("class"),
    ETC("etx");

    private final String type;

    public static ContactType of (String type){
        return Stream.of(ContactType.values())
                .filter(a -> a.getType().equals(type))
                .findFirst()
                .orElseThrow(() -> new ApplicationException(ErrorCode.CONTACT_TYPE_NOT_FOUND));

    }
}
