package com.example.noticeboard.dto.request;

import com.example.noticeboard.dto.CommentDto;
import com.example.noticeboard.dto.UserAccountDto;

/**
 * A DTO for the {@link com.example.noticeboard.domain.Comment} entity
 */
public record CommentRequest(Long articleId, String content) {
    public static CommentRequest of(Long articleId, String content) {
        return new CommentRequest(articleId,content);
    }

    public CommentDto toDto(UserAccountDto userAccountDto) {
        return CommentDto.of(
                articleId,
                userAccountDto,
                content
        );
    }
}