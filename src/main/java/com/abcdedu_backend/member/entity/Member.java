package com.abcdedu_backend.member.entity;

import com.abcdedu_backend.post.entity.Comment;
import com.abcdedu_backend.post.entity.Post;
import com.abcdedu_backend.utils.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
//@SQLDelete(sql = "UPDATE members SET deleted = true WHERE id = ?") // 복잡한 로직 수정이 어려워 제거
//@SQLRestriction("deleted = false")
@Table(name = "members")
public class Member extends BaseTimeEntity {

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

    @Column(name = "deleted")
    private boolean deleted;

    @OneToMany(mappedBy = "member")
    private List<Post> posts;

    @OneToMany(mappedBy = "member")
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

    public void updateProfile(String name, String imageUrl, String school, Long studentId){
        this.name = name;
        this.imageObjectKey = imageUrl;
        this.school = school;
        this.studentId = studentId;
    }

    public void updateRole(MemberRole memberRole) {
        this.role = memberRole;
    }

    public void updatePassword(String newEncodedPassword){
        this.encodedPassword = newEncodedPassword;
    }

    public void delete() {
        this.deleted = true;
    }

}
