package com.example.noticeboard.dto;

import com.example.noticeboard.domain.Article;
import com.example.noticeboard.domain.Hashtag;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A DTO for the {@link Article} entity
 */
public record ArticleWithCommentDto(
        Long id,
        UserAccountDto userAccountDto,
        Set<CommentDto> articleWithCommentDtos,
        String title,
        String content,
        Set<HashtagDto> hashtags,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime modifiedAt,
        String modifiedBy
) {
    public static ArticleWithCommentDto of(
            Long id,
            UserAccountDto userAccountDto,
            Set<CommentDto> articleWithCommentDtos,
            String title,
            String content,
            Set<HashtagDto> hashtags,
            LocalDateTime createdAt,
            String createdBy,
            LocalDateTime modifiedAt,
            String modifiedBy
    ) {
        return new ArticleWithCommentDto(id, userAccountDto, articleWithCommentDtos, title, content, hashtags, createdAt, createdBy, modifiedAt, modifiedBy);
    }

    public static ArticleWithCommentDto of(
            UserAccountDto userAccountDto,
            Set<CommentDto> articleWithCommentDtos,
            String title,
            String content,
            Set<HashtagDto> hashtags,
            LocalDateTime createdAt,
            String createdBy
    ) {
        return new ArticleWithCommentDto(null, userAccountDto, articleWithCommentDtos, title, content, hashtags, createdAt, createdBy, null, null);
    }

    public static ArticleWithCommentDto of(
            Long id,
            UserAccountDto userAccountDto,
            String title,
            String content,
            Set<HashtagDto> hashtags,
            LocalDateTime createdAt,
            String createdBy
    ) {
        return new ArticleWithCommentDto(id, userAccountDto, Set.of(), title, content, hashtags, createdAt, createdBy, null, null);
    }

    public static ArticleWithCommentDto from(Article entity) {
        return new ArticleWithCommentDto(
                entity.getId(),
                UserAccountDto.from(entity.getUserAccount()),
                entity.getComments().stream()
                        .map(CommentDto::from)
                        .collect(Collectors.toCollection(LinkedHashSet::new)),
                entity.getTitle(),
                entity.getContent(),
                entity.getHashtags().stream().map(HashtagDto::from).collect(Collectors.toSet()),
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