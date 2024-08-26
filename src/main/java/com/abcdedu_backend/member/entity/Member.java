package com.abcdedu_backend.member.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false, length = 10)
    private String name;

    @Column(nullable = false, length = 40)
    private String email;

    @Column(nullable = false, length = 60)
    private String encodedPassword;

    @Column(length = 10)
    private String school;

    @Column
    private Long studentId;

    @Column(length=100)
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

}
