package com.example.noticeboard.dto;

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
}