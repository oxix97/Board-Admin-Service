package com.example.adminservice.dto.response;

import com.example.adminservice.dto.ArticleCommentDto;
import com.example.adminservice.dto.UserAccountDto;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ArticleCommentResponse(
        Long id,
        UserAccountDto userAccount,
        String content,
        LocalDateTime createdAt
) {

    public static ArticleCommentResponse of(Long id, UserAccountDto userAccount, String content, LocalDateTime createdAt) {
        return new ArticleCommentResponse(id, userAccount, content, createdAt);
    }

    public static ArticleCommentResponse of(ArticleCommentDto dto) {
        return ArticleCommentResponse.of(dto.id(), dto.userAccountDto(), dto.content(), dto.createdAt());
    }

}