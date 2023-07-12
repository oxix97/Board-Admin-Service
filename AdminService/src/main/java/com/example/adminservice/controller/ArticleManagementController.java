package com.example.adminservice.controller;

import com.example.adminservice.dto.ArticleDto;
import com.example.adminservice.dto.response.ArticleResponse;
import com.example.adminservice.service.ArticleManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/management/articles")
@Controller
public class ArticleManagementController {

    private final ArticleManagementService service;

    @GetMapping
    public String managementAdmin(Model model) {
        model.addAttribute(
                "articles",
                service.getArticles().stream()
                        .map(ArticleResponse::withoutContent)
                        .toList()
        );
        return "management/articles";
    }

    @ResponseBody
    @GetMapping("/{id}")
    public ArticleResponse article(@PathVariable Long id) {
        return ArticleResponse.withContent(service.getArticle(id));
    }

    @PostMapping("/{id}")
    public String deleteArticle(@PathVariable Long id) {
        service.deleteArticle(id);
        return "redirect:/management/articles";
    }
}
