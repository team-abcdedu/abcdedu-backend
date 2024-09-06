package com.abcdedu_backend.contact.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ContactGetResponse(
         String type,
         String title,
         String userName,
         String phoneNumber,
         String email,
         String content,
         LocalDateTime createdAt
) {
}
