package com.example.noticeboard.controller;

import com.example.noticeboard.domain.security.BoardPrincipal;
import com.example.noticeboard.dto.request.ArticleCommentRequest;
import com.example.noticeboard.service.ArticleCommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/comments")
@Controller
public class ArticleCommentController {
    private final ArticleCommentService articleCommentService;

    @PostMapping("/new")
    public String postNewComment(
            @AuthenticationPrincipal BoardPrincipal boardPrincipal,
            ArticleCommentRequest request
    ) {
        articleCommentService.saveComment(
                request.toDto(boardPrincipal.toDto())
        );
        return "redirect:/articles/" + request.articleId();
    }

    @DeleteMapping("/{commentId}/delete")
    public String deleteComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal BoardPrincipal boardPrincipal,
            Long articleId
    ) {
        articleCommentService.deleteComment(commentId, boardPrincipal.getUsername());
        return "redirect:/articles/" + articleId;
    }
}