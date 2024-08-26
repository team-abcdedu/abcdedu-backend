package com.abcdedu_backend.member.entity;

import com.abcdedu_backend.comment.Comment;
import com.abcdedu_backend.post.Post;
import com.abcdedu_backend.utils.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member extends BaseTimeEntity {

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

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;


    public void updateProfile(String name, String imageUrl, String school, Long studentId){
        this.name = name;
        this.imageUrl = imageUrl;
        this.school = school;
        this.studentId = studentId;
    }
}
