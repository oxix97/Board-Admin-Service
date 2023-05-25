package com.example.noticeboard.dto.response;

import com.example.noticeboard.dto.ArticleDto;
import com.example.noticeboard.dto.HashtagDto;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public record ArticleResponse(
        Long id,
        String title,
        String content,
        Set<HashtagDto> hashtags,
        LocalDateTime createdAt,
        String email,
        String nickname
) {

    public static ArticleResponse of(Long id, String title, String content, Set<HashtagDto> hashtags, LocalDateTime createdAt, String email, String nickname) {
        return new ArticleResponse(id, title, content, hashtags, createdAt, email, nickname);
    }

    public static ArticleResponse from(ArticleDto dto) {
        String nickname = dto.userAccountDto().nickname();
        if (nickname == null || nickname.isBlank()) {
            nickname = dto.userAccountDto().userId();
        }

        return new ArticleResponse(
                dto.id(),
                dto.title(),
                dto.content(),
                dto.hashtagDtos(),
                dto.createdAt(),
                dto.userAccountDto().email(),
                nickname
        );
    }

}
