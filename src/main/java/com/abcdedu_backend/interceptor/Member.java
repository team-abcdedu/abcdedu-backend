package com.abcdedu_backend.interceptor;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String encodedPassword;
    private String school;
    private Long studentId;
    private Date created_at;
    @Enumerated(EnumType.STRING)
    private MemberRole role;

    // interceptor 전용 Member 생성
    public Member(String name, String email, MemberRole role) {
        this.name = name;
        this.email = email;
        this.role = role;
    }

}
