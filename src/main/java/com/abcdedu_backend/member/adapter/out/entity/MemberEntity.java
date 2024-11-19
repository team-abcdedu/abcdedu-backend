package com.abcdedu_backend.member.adapter.out.entity;

import com.abcdedu_backend.member.application.domain.Member;
import com.abcdedu_backend.member.application.domain.MemberRole;
import com.abcdedu_backend.post.entity.Comment;
import com.abcdedu_backend.post.entity.Post;
import com.abcdedu_backend.utils.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "members")
public class MemberEntity extends BaseTimeEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false, length = 10)
    private String name;

    @Column(nullable = false, length = 40, unique = true)
    private String email;

    @Column(nullable = false, length = 60)
    private String encodedPassword;

    @Column(length = 20)
    private String school;

    @Column
    private Long studentId;

    @Column(length=100)
    private String imageObjectKey;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    public boolean isAdmin(){
        return this.role == MemberRole.ADMIN;
    }

    public boolean isBasic() {
        return this.role == MemberRole.BASIC;
    }
    public boolean isStudent() {
        return this.role == MemberRole.STUDENT;
    }

    public void updateProfile(String name, String school, Long studentId, String imageObjectKey){
        this.name = name;
        this.imageObjectKey = imageObjectKey;
        this.school = school;
        this.studentId = studentId;
    }

    public void updateRole(MemberRole memberRole) {
        this.role = memberRole;
    }

    public void updatePassword(String newEncodedPassword){
        this.encodedPassword = newEncodedPassword;
    }

    public static MemberEntity from(Member member) {
        return MemberEntity.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .role(member.getRole())
                .encodedPassword(member.getEncodedPassword())
                .imageObjectKey(member.getImageObjectKey())
                .school(member.getSchool())
                .studentId(member.getStudentId())
                .posts(new ArrayList<>())
                .comments(new ArrayList<>())
                .build();
    }

    public Member toDomain() {
        return Member.builder()
                .id(id)
                .name(name)
                .email(email)
                .role(role)
                .imageObjectKey(imageObjectKey)
                .school(school)
                .studentId(studentId)
                .createdAt(this.getCreatedAt())
                .postCount(this.posts.size())
                .commentCount(this.comments.size())
                .build();
    }

}

