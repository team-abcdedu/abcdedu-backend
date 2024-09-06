package com.abcdedu_backend.contact.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ContactListResponse (
        String type,
        Long contactId,
        String title,
        String userName,

        LocalDateTime createdAt

)
{
}
