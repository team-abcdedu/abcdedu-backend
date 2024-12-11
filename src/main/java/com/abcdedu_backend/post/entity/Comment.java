package com.abcdedu_backend.post.entity;

import com.abcdedu_backend.member.entity.Member;
import com.abcdedu_backend.utils.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;


@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE comments SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
@Table(name = "comments")
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member; // 작성자를 나타낸다.

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(name = "content", length = 200, nullable = false)
    private String content;

    @Column(length=100)
    private String fileObjectKey;

    @Column(name = "deleted")
    private boolean deleted = false;  // 소프트 삭제 여부를 나타내는 필드

    public void updateContent(String content) {
        this.content = content;
    }

    public void updateFileObjectKey(String fileObjectKey) {
        this.fileObjectKey = fileObjectKey;
    }

    public boolean hasFile() {
        return !(fileObjectKey == null || fileObjectKey.isEmpty());
    }


}
