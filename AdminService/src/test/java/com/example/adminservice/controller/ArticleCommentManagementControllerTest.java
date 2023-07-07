package com.example.adminservice.controller;

import com.example.adminservice.config.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("[VIEW] 댓글 관리")
@Import(SecurityConfig.class)
@WebMvcTest(ArticleCommentManagementController.class)
class ArticleCommentManagementControllerTest {
    private final MockMvc mvc;

    public ArticleCommentManagementControllerTest(MockMvc mvc) {
        this.mvc = mvc;
    }

    //    @DisplayName("[GET] 게시글 관리 페이지 - 정상 호출")
    @Test
    void givenNothing_whenRequestingArticleManagementView_thenReturnView() throws Exception {
        //given

        //when
        mvc.perform(get("/management/article-comments"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("management/article-comments"));

        //then

    }
}