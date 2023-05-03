package com.example.noticeboard.dto;

import com.example.noticeboard.domain.Article;
import com.example.noticeboard.domain.UserAccount;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A DTO for the {@link com.example.noticeboard.domain.Article} entity
 */
public record ArticleDto(
        Long id,
        String title,
        String content,
        String hashtag,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime modifiedAt,
        String modifiedBy
) {
    public static ArticleDto of(
            Long id,
            String title,
            String content,
            String hashtag,
            LocalDateTime createdAt,
            String createdBy,
            LocalDateTime modifiedAt,
            String modifiedBy
    ) {
        return new ArticleDto(id, title, content, hashtag, createdAt, createdBy, modifiedAt, modifiedBy);
    }

    public static ArticleDto of(
            String title,
            String content,
            String hashtag,
            LocalDateTime createdAt,
            String createdBy
    ) {
        return new ArticleDto(null, title, content, hashtag, createdAt, createdBy, null, null);
    }

    public static ArticleDto from(Article entity) {
        return new ArticleDto(
                entity.getId(),
                entity.getTitle(),
                entity.getContent(),
                entity.getHashtag(),
                entity.getCreatedAt(),
                entity.getCreatedBy(),
                entity.getModifiedAt(),
                entity.getModifiedBy()
        );
    }

//    public Article toEntity(UserAccount userAccount) {
//        return Article.of(
//                userAccount,
//                title,
//                content,
//                hashtag
//        );
//    }
}