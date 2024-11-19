package com.abcdedu_backend.member.adapter.out.entity;

import com.abcdedu_backend.member.domain.EmailCode;
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
