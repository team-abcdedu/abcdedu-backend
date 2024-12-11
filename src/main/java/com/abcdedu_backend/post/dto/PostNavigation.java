package com.abcdedu_backend.post.dto;

import com.abcdedu_backend.post.entity.Post;

public record PostNavigation(
        Long id,
        String title,
        String writerEmail,
        boolean secret
) {
    public static PostNavigation of(Post post) {
        if (post == null) {
            return null;
        }

        return new PostNavigation(post.getId(), post.getTitle(), post.getMember().getEmail(), post.getSecret());
    }
}
