package com.example.adminservice.controller;

import com.example.adminservice.config.SecurityConfig;
import com.example.adminservice.dto.ArticleDto;
import com.example.adminservice.dto.UserAccountDto;
import com.example.adminservice.service.ArticleManagementService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("[VIEW] 게시글 관리")
@Import(SecurityConfig.class)
@WebMvcTest(ArticleManagementController.class)
class ArticleManagementControllerTest {
    private final MockMvc mvc;

    @MockBean
    private ArticleManagementService service;

    public ArticleManagementControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @DisplayName("[GET] 게시글 관리 페이지 - 정상 호출")
    @Test
    void givenNothing_whenRequestingArticlesManagementView_thenReturnView() throws Exception {
        //given
        given(service.getArticles()).willReturn(List.of());

        //when
        mvc.perform(get("/management/articles"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("management/articles"))
                .andExpect(model().attribute("articles", List.of()));

        //then
        then(service).should().getArticles();
    }

    /*
     * 1. 단건 게시글 조회
     * 2. 게시글 삭제
     * */

    @DisplayName("[GET] 게시글 단건 - 정상 호출")
    @Test
    void givenNothing_whenRequestingArticleManagementView_thenReturnView() throws Exception {
        //given
        Long id = 1L;
        ArticleDto article = createArticleDto("title", "content");
        given(service.getArticle(id)).willReturn(article);

        //when
        mvc.perform(get("/management/articles/" + id))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.title").value(article.title()))
                .andExpect(jsonPath("$.content").value(article.content()))
                .andExpect(jsonPath("$.userAccount.nickname").value(article.userAccount().nickname()));

        //then
        then(service).should().getArticle(id);
    }

    @DisplayName("[DELETE] 게시글 삭제 - 정상 호출")
    @Test
    void givenNothing_whenRequestingDeletion_thenRedirectsToArticleManagementView() throws Exception {
        //given
        Long id = 1L;
        willDoNothing().given(service).deleteArticle(id);

        //when
        mvc.perform(post("/management/articles/" + id).with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/management/articles"))
                .andExpect(redirectedUrl("/management/articles"));

        //then
        then(service).should().deleteArticle(id);
    }


    private ArticleDto createArticleDto(String title, String content) {
        return ArticleDto.of(
                1L,
                createUserAccountDto(),
                title,
                content,
                null,
                LocalDateTime.now(),
                "test",
                LocalDateTime.now(),
                "test"
        );
    }

    private UserAccountDto createUserAccountDto() {
        return UserAccountDto.of(
                "test1",
                "pw",
                "test-title",
                "test-content",
                "test-memo"
        );
    }
}