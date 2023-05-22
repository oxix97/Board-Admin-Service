package com.example.noticeboard.controller;

import com.example.noticeboard.config.SecurityConfig;
import com.example.noticeboard.dto.ArticleDto;
import com.example.noticeboard.dto.ArticleWithCommentDto;
import com.example.noticeboard.dto.UserAccountDto;
import com.example.noticeboard.service.ArticleService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Set;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(SecurityConfig.class)
@DisplayName("View Controller - 게시글")
@WebMvcTest(ArticleController.class)
class ArticleControllerTest {
    private final MockMvc mvc;

    @MockBean
    private ArticleService service;

    public ArticleControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @DisplayName("[GET] 게시글 리스트 - 정상 호출")
    @Test
    void test1() throws Exception {
        //given
        given(service.searchArticles(eq(null), eq(null), any(Pageable.class))).willReturn(Page.empty());

        //when & then
        mvc.perform(get("/articles"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/index"))
                .andExpect(model().attributeExists("articles"));
//                .andExpect(model().attributeExists("searchTypes"));

        then(service).should().searchArticles(eq(null), eq(null), any(Pageable.class));
    }

    @DisplayName("[GET] 게시글 단건[1] - 정상 호출")
    @Test
    void test2() throws Exception {
        Long articleId = 1L;
        mvc.perform(get("/articles/" + articleId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(model().attributeExists("articles"));
    }

    @DisplayName("[GET] 게시글 상세페이지 - 정상 호출")
    @Test
    void test3() throws Exception {
        Long id = 1L;
        //given
        given(service.getArticle(id)).willReturn(createArticleWithCommentsDto());

        //when
        mvc.perform(get("/articles/"+id))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/detail"))
                .andExpect(model().attributeExists("article"))
                .andExpect(model().attributeExists("comments"));

        //then
        then(service).should().getArticle(id);

    }

    @DisplayName("[GET] 게시글 검색 전용 페이지 - 정상 호출")
    @Test
    void test4() throws Exception {
        mvc.perform(get("/articles"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(model().attributeExists("articles"));
    }

    @DisplayName("[GET] 게시글 해시태그 검색 페이지 - 정상 호출")
    @Test
    void test5() throws Exception {
        mvc.perform(get("/articles/hashtag"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(model().attributeExists("articles"));
    }

    private ArticleWithCommentDto createArticleWithCommentsDto() {
        return ArticleWithCommentDto.of(
                1L,
                createUserAccountDto(),
                "title",
                "content",
                "hashtag",
                LocalDateTime.now(),
                "uno"
        );
    }

    private UserAccountDto createUserAccountDto() {
        return UserAccountDto.of(
                "uno",
                "pw",
                "uno@mail.com",
                "Uno",
                "memo",
                LocalDateTime.now(),
                "uno",
                LocalDateTime.now(),
                "uno"
        );
    }
}