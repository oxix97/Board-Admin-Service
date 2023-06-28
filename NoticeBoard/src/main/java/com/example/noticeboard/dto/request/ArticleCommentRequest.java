package com.example.noticeboard.dto.request;

import com.example.noticeboard.domain.ArticleComment;
import com.example.noticeboard.dto.ArticleCommentDto;
import com.example.noticeboard.dto.UserAccountDto;

/**
 * A DTO for the {@link ArticleComment} entity
 */
public record ArticleCommentRequest(
        Long articleId,
        Long parentCommentId,
        String content
) {
    public static ArticleCommentRequest of(Long articleId, String content) {
        return new ArticleCommentRequest(articleId, null, content);
    }

    public static ArticleCommentRequest of(Long articleId, Long parentCommentId, String content) {
        return new ArticleCommentRequest(articleId, parentCommentId, content);
    }

    public ArticleCommentDto toDto(UserAccountDto userAccountDto) {
        return ArticleCommentDto.of(
                articleId,
                userAccountDto,
                parentCommentId,
                content
        );
    }
}