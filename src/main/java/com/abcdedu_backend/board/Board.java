package com.abcdedu_backend.board;

import com.abcdedu_backend.post.Post;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="boards")
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "board")
    private List<Post> posts;

    public static Board of(String name) {
        return Board.builder()
                .name(name)
                .build();
    }

}