package com.example.adminservice.controller;

import com.example.adminservice.dto.response.ArticleCommentResponse;
import com.example.adminservice.service.ArticleCommentManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/management/article-comments")
@RequiredArgsConstructor
@Controller
public class ArticleCommentManagementController {

    private final ArticleCommentManagementService service;

    @GetMapping
    public String articleComments(Model model) {
        model.addAttribute(
                "article-comments",
                service.getArticleComments().stream()
                        .map(ArticleCommentResponse::of)
                        .toList()
        );

        return "management/article-comments";
    }

    @ResponseBody
    @GetMapping("/{id}")
    public ArticleCommentResponse getArticleComment(@PathVariable Long id) {
        return ArticleCommentResponse.of(service.getArticleComment(id));
    }

    @PostMapping("/{id}")
    public String deleteArticleComment(@PathVariable Long id) {
        service.deleteArticleComment(id);
        return "redirect:/management/article-comments";
    }
}
