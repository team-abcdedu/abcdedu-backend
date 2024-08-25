package com.abcdedu_backend.interceptor;

import com.abcdedu_backend.utils.BaseTimeEntity;
import com.abcdedu_backend.post.Post;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "members")
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String encodedPassword;
    private String school;
    private Long studentId;
    @Enumerated(EnumType.STRING)
    private MemberRole role;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts;

    public Member() {
    }

    // interceptor 전용 Member 생성
    public Member(String name, String email, MemberRole role) {
        this.name = name;
        this.email = email;
        this.role = role;
    }

}
