package com.example.noticeboard.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A DTO for the {@link com.example.noticeboard.domain.Article} entity
 */
public record ArticleDto(
        String title,
        String content,
        String hashtag,
        LocalDateTime createdAt,
        String createdBy
) {
    public static ArticleDto of(String title, String content, String hashtag, LocalDateTime createdAt, String createdBy) {
        return new ArticleDto(title, content, hashtag, createdAt, createdBy);
    }
}