package com.example.noticeboard.controller;

import com.example.noticeboard.domain.type.FormStatus;
import com.example.noticeboard.domain.type.SearchType;
import com.example.noticeboard.dto.ArticleDto;
import com.example.noticeboard.dto.UserAccountDto;
import com.example.noticeboard.dto.request.ArticleRequest;
import com.example.noticeboard.dto.response.ArticleResponse;
import com.example.noticeboard.dto.response.ArticleWithCommentsResponse;
import com.example.noticeboard.service.ArticleService;
import com.example.noticeboard.service.PaginationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/articles")
@Controller
public class ArticleController {
    private final ArticleService articleService;
    private final PaginationService paginationService;

    @GetMapping
    public String articles(
            @RequestParam(required = false) SearchType searchType,
            @RequestParam(required = false) String searchValue,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            ModelMap map
    ) {
        Page<ArticleResponse> articles = articleService.searchArticles(searchType, searchValue, pageable).map(ArticleResponse::from);
        List<Integer> barNumbers = paginationService.getPaginationBarNumbers(pageable.getPageNumber(), articles.getTotalPages());

        map.addAttribute("articles", articles);
        map.addAttribute("paginationBarNumbers", barNumbers);
        map.addAttribute("searchTypes", SearchType.values());

        return "articles/index";
    }

    @GetMapping("/{articleId}")
    public String article(
            @PathVariable Long articleId,
            ModelMap map
    ) {
        ArticleWithCommentsResponse article = ArticleWithCommentsResponse.from(articleService.getArticleWithComments(articleId));

        map.addAttribute("article", article);
        map.addAttribute("comments", article.articleCommentsResponse());
        return "articles/detail";
    }

    @GetMapping("/search-hashtag")
    public String searchHashtag(
            @RequestParam(required = false) String searchValue,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            ModelMap map
    ) {
        Page<ArticleResponse> articles = articleService
                .searchArticlesViaHashtag(searchValue, pageable)
                .map(ArticleResponse::from);
        List<String> hashtags = articleService.getHashtags();
        List<Integer> barNumbers = paginationService.getPaginationBarNumbers(pageable.getPageNumber(), articles.getTotalPages());
        map.addAttribute("articles", articles);
        map.addAttribute("hashtags", hashtags);
        map.addAttribute("paginationBarNumbers", barNumbers);

        return "articles/search-hashtag";
    }


    @PostMapping("/{articleId}/form")
    public String updateArticle(
            @PathVariable Long articleId,
            ArticleRequest request
    ) {
        // TODO: 2023/05/25 인증 정보를 추후에 넣어야 한다.

//        articleService.updateArticle(articleId, request.toDto(UserAccountDto.from(request.)));
        return "redirect:/articles/" + articleId;
    }

    @GetMapping("/{articleId}/form")
    public String updateArticleForm(
            @PathVariable Long articleId,
            ModelMap map
    ) {

        ArticleResponse article = ArticleResponse.from(articleService.getArticle(articleId));

        map.addAttribute("article", article);
        map.addAttribute("formStatus", FormStatus.UPDATE);
        return "articles/form";
    }

}
