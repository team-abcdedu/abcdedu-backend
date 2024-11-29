package com.abcdedu_backend.member.application.dto.command;


import org.springframework.web.multipart.MultipartFile;

public record UpdateMemberInfoCommand (
        Long memberId,
        String name,
        String school,
        Long studentId,
        MultipartFile file
){
    public static UpdateMemberInfoCommand of(Long memberId, String name, String school, Long studentId, MultipartFile file) {
        return new UpdateMemberInfoCommand(memberId, name, school, studentId, file);

    }
}
