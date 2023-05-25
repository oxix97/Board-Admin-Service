package com.example.noticeboard.controller;

import com.example.noticeboard.dto.CommentDto;
import com.example.noticeboard.dto.UserAccountDto;
import com.example.noticeboard.dto.request.CommentRequest;
import com.example.noticeboard.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/comments")
@Controller
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/new")
    public String postNewComment(
            @RequestBody CommentRequest request
    ) {
        UserAccountDto dto = UserAccountDto.of(
                "uno",
                "pw",
                "uno@mail.com",
                "uno",
                "alkjsdflkajdf"
        );
        commentService.saveComment(request.toDto(dto));

        return "redirect:/articles/" + request.articleId();
    }

    @DeleteMapping("/{commentId}/delete")
    public String deleteComment(
            @PathVariable Long commentId,
            Long articleId
    ) {
        commentService.deleteComment(commentId);
        return "redirect:/articles" + articleId;
    }
}