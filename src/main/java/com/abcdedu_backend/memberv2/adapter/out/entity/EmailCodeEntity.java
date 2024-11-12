package com.abcdedu_backend.memberv2.adapter.out.entity;

import com.abcdedu_backend.memberv2.application.domain.EmailCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@AllArgsConstructor
@RedisHash(value = "email_code", timeToLive = 300)
public class EmailCodeEntity {

    @Id
    private String email;
    private String code;

    public EmailCode toDomain() {
        return new EmailCode(email, code);
    }
}
