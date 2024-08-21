package com.abcdedu_backend.member.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor()
public class Member {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false, length = 10)
    private String name;

    @Column(nullable = false, length = 40)
    private String email;

    @Column(nullable = false, length = 50)
    private String encodedPassword;

    @Column(length = 10)
    private String school;

    @Column
    private Long student_id;

    @Column(length=100)
    private String image_url;

    private MemberRole role;

    @Builder
    public Member(String name, String email, String encodedPassword) {
        this.name = name;
        this.email = email;
        this.encodedPassword = encodedPassword;
        this.role = MemberRole.BASIC;
    }
}
