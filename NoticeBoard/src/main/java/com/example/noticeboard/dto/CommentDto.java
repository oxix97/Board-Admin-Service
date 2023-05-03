package com.example.noticeboard.dto;

import com.example.noticeboard.domain.Comment;

import java.time.LocalDateTime;

/**
 * A DTO for the {@link com.example.noticeboard.domain.Comment} entity
 */
public record CommentDto(
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime modifiedAt,
        String modifiedBy,
        String content
) {
    public static CommentDto of(LocalDateTime createdAt, String createdBy, LocalDateTime modifiedAt, String modifiedBy, String content) {
        return new CommentDto(createdAt, createdBy, modifiedAt, modifiedBy, content);
    }

    public static CommentDto from(Comment entity) {
        return new CommentDto(
                entity.getCreatedAt(),
                entity.getCreatedBy(),
                entity.getModifiedAt(),
                entity.getModifiedBy(),
                entity.getContent()
        );
    }
}