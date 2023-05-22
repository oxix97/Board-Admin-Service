package com.example.noticeboard.dto;

import com.example.noticeboard.domain.Article;
import com.example.noticeboard.domain.UserAccount;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * A DTO for the {@link com.example.noticeboard.domain.Article} entity
 */
public record ArticleDto(
        Long id,
        UserAccountDto userAccountDto,
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
            UserAccountDto userAccountDto,
            String title,
            String content,
            String hashtag,
            LocalDateTime createdAt,
            String createdBy,
            LocalDateTime modifiedAt,
            String modifiedBy
    ) {
        return new ArticleDto(id, userAccountDto, title, content, hashtag, createdAt, createdBy, modifiedAt, modifiedBy);
    }

    public static ArticleDto of(
            String title,
            UserAccountDto userAccountDto,
            String content,
            String hashtag,
            LocalDateTime createdAt,
            String createdBy
    ) {
        return new ArticleDto(null, userAccountDto, title, content, hashtag, createdAt, createdBy, null, null);
    }

    public static ArticleDto from(Article entity) {
        return new ArticleDto(
                entity.getId(),
                UserAccountDto.from(entity.getUserAccount()),
                entity.getTitle(),
                entity.getContent(),
                entity.getHashtag(),
                entity.getCreatedAt(),
                entity.getCreatedBy(),
                entity.getModifiedAt(),
                entity.getModifiedBy()
        );
    }

    public Article toEntity() {
        return Article.of(
                userAccountDto.toEntity(userAccountDto),
                title,
                content,
                hashtag
        );
    }
}