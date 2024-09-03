package com.abcdedu_backend.contact.dto.response;

import com.abcdedu_backend.contact.entity.ContactType;
import lombok.Builder;

@Builder
public record ContactResponse (
         String userName,
         String phoneNumber,
         String email,
         String title,
         String content,
         ContactType contactType
) {
}
