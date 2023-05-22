package com.example.noticeboard.dto;

import com.example.noticeboard.domain.Article;
import com.example.noticeboard.domain.Comment;
import com.example.noticeboard.domain.UserAccount;

import java.time.LocalDateTime;

public record CommentDto(
        Long id,
        Long articleId,
        UserAccountDto userAccountDto,
        String content,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime modifiedAt,
        String modifiedBy
) {

    public static CommentDto of(Long articleId, UserAccountDto userAccountDto, String content) {
        return CommentDto.of(articleId, userAccountDto, null, content);
    }

    public static CommentDto of(Long articleId, UserAccountDto userAccountDto, Long parentCommentId, String content) {
        return CommentDto.of(null, articleId, userAccountDto, parentCommentId, content, null, null, null, null);
    }

    public static CommentDto of(Long id, Long articleId, UserAccountDto userAccountDto, Long parentCommentId, String content, LocalDateTime createdAt, String createdBy, LocalDateTime modifiedAt, String modifiedBy) {
        return new CommentDto(id, articleId, userAccountDto, content, createdAt, createdBy, modifiedAt, modifiedBy);
    }

    public static CommentDto from(Comment entity) {
        return new CommentDto(
                entity.getId(),
                entity.getArticle().getId(),
                UserAccountDto.from(entity.getUserAccount()),
                entity.getContent(),
                entity.getCreatedAt(),
                entity.getCreatedBy(),
                entity.getModifiedAt(),
                entity.getModifiedBy()
        );
    }

    public Comment toEntity(Article article, UserAccount userAccount) {
        return Comment.of(
                article,
                userAccount,
                content
        );
    }

}