package com.abcdedu_backend.contact.dto.response;

import com.abcdedu_backend.contact.entity.ContactType;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ContactListResponse (
        ContactType contactType,
        String title,
        String userName,

        LocalDateTime createdAt

)
{
}
