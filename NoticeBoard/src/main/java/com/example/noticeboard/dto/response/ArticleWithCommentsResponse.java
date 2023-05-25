package com.example.noticeboard.dto.response;

import com.example.noticeboard.domain.Hashtag;
import com.example.noticeboard.dto.ArticleWithCommentDto;
import com.example.noticeboard.dto.CommentDto;
import com.example.noticeboard.dto.HashtagDto;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public record ArticleWithCommentsResponse(
        Long id,
        String title,
        String content,
        Set<HashtagDto> hashtags,
        LocalDateTime createdAt,
        String email,
        String nickname,
        String userId,
        Set<CommentResponse> articleCommentsResponse
) {

    public static ArticleWithCommentsResponse of(Long id, String title, String content, Set<HashtagDto> hashtags, LocalDateTime createdAt, String email, String nickname, String userId, Set<CommentResponse> CommentResponses) {
        return new ArticleWithCommentsResponse(id, title, content, hashtags, createdAt, email, nickname, userId, CommentResponses);
    }

    public static ArticleWithCommentsResponse from(ArticleWithCommentDto dto) {
        String nickname = dto.userAccountDto().nickname();
        if (nickname == null || nickname.isBlank()) {
            nickname = dto.userAccountDto().userId();
        }

        return new ArticleWithCommentsResponse(
                dto.id(),
                dto.title(),
                dto.content(),
                dto.hashtags(),
                dto.createdAt(),
                dto.userAccountDto().email(),
                nickname,
                dto.userAccountDto().userId(),
                dto.articleWithCommentDtos().stream()
                        .map(CommentResponse::from)
                        .collect(Collectors.toCollection(LinkedHashSet::new))
        );
    }


    private static Set<CommentResponse> organizeChildComments(Set<CommentDto> dtos) {
        Map<Long, CommentResponse> map = dtos.stream()
                .map(CommentResponse::from)
                .collect(Collectors.toMap(CommentResponse::id, Function.identity()));

        map.values().stream()
                .filter(CommentResponse::hasParentComment)
                .forEach(comment -> {
                    CommentResponse parentComment = map.get(comment.parentCommentId());
                    parentComment.childComments().add(comment);
                });

        return map.values().stream()
                .filter(comment -> !comment.hasParentComment())
                .collect(Collectors.toCollection(() ->
                        new TreeSet<>(Comparator
                                .comparing(CommentResponse::createdAt)
                                .reversed()
                                .thenComparingLong(CommentResponse::id)
                        )
                ));
    }
}
