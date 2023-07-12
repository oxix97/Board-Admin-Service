package com.example.adminservice.dto.response;

import com.example.adminservice.dto.ArticleDto;
import com.example.adminservice.dto.UserAccountDto;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ArticleResponse(
        Long id,
        UserAccountDto userAccount,
        String title,
        String content,
        LocalDateTime createdAt
) {
    public static ArticleResponse of(
            Long id,
            UserAccountDto userAccount,
            String title,
            String content,
            LocalDateTime createdAt
    ) {
        return new ArticleResponse(id, userAccount, title, content, createdAt);
    }

    public static ArticleResponse withContent(ArticleDto dto) {
        return ArticleResponse.of(
                dto.id(),
                dto.userAccount(),
                dto.title(),
                dto.content(),
                dto.createdAt()
        );
    }

    public static ArticleResponse withoutContent(ArticleDto dto) {
        return ArticleResponse.of(
                dto.id(),
                dto.userAccount(),
                dto.title(),
                null,
                dto.createdAt()
        );
    }
}
