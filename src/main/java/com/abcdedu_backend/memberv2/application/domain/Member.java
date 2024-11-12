package com.abcdedu_backend.memberv2.application.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class Member {

    private Long id;

    private String name;

    private String email;

    private String encodedPassword;

    private String school;

    private Long studentId;

    private String imageObjectKey;

    private MemberRole role;

    private Integer postCount;

    private Integer commentCount;

    private LocalDateTime createdAt;

    public boolean isAdmin(){
        return this.role == MemberRole.ADMIN;
    }

    public boolean isBasic() {
        return this.role == MemberRole.BASIC;
    }
}
