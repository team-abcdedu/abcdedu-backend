package com.abcdedu_backend.infra.file;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FileDirectory {

    PROFILE_IMAGE("abcdedu/profile/");

    private final String directoryName;
}
