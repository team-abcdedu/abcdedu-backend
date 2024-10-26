package com.abcdedu_backend.member.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@AllArgsConstructor
@RedisHash(value = "email_code", timeToLive = 300)
public class EmailCode {

    @Id
    private String email;
    private String code;
}
