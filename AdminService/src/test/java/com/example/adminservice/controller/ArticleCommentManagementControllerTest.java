package com.example.adminservice.controller;

import com.example.adminservice.config.GlobalControllerConfig;
import com.example.adminservice.config.TestSecurityConfig;
import com.example.adminservice.dto.ArticleCommentDto;
import com.example.adminservice.dto.UserAccountDto;
import com.example.adminservice.service.ArticleCommentManagementService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("[VIEW] 댓글 관리")
@Import({TestSecurityConfig.class, GlobalControllerConfig.class})
@WebMvcTest(ArticleCommentManagementController.class)
class ArticleCommentManagementControllerTest {
    private final MockMvc mvc;

    @MockBean
    private ArticleCommentManagementService service;

    public ArticleCommentManagementControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @WithMockUser(value = "test", roles = "USER")
    @DisplayName("[GET] 댓글 관리 페이지 - 정상 호출")
    @Test
    void givenNothing_whenRequestingArticleManagementView_thenReturnView() throws Exception {
        //given
        given(service.getArticleComments()).willReturn(List.of());

        //when
        mvc.perform(get("/management/article-comments"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("management/article-comments"))
                .andExpect(model().attribute("article-comments", List.of()));

        //then
        then(service).should().getArticleComments();
    }

    @WithMockUser(value = "test", roles = "ADMIN")
    @DisplayName("[GET] 단일 댓글 관리 페이지 - 정상 호출")
    @Test
    void givenArticleCommentId_whenRequestingArticleCommentManagementView_thenReturnView() throws Exception {
        //given
        Long id = 1L;
        ArticleCommentDto comment = createArticleCommentDto("댓글");
        given(service.getArticleComment(id)).willReturn(comment);

        //when
        mvc.perform(get("/management/article-comments/" + id))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.content").value(comment.content()));

        //then
        then(service).should().getArticleComment(id);
    }

    @WithMockUser(value = "test", roles = "ADMIN")
    @DisplayName("[DELETE] 댓글 삭제 - 정상 호출")
    @Test
    void givenNothing_whenRequestingDeletion_thenRedirectsToArticleCommentManagementView() throws Exception {
        //given
        Long id = 1L;
        willDoNothing().given(service).deleteArticleComment(id);

        //when
        mvc.perform(post("/management/article-comments/" + id).with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/management/article-comments"))
                .andExpect(redirectedUrl("/management/article-comments"));

        //then
        then(service).should().deleteArticleComment(id);
    }


    private ArticleCommentDto createArticleCommentDto(String content) {
        return ArticleCommentDto.of(
                1L,
                1L,
                createUserAccountDto(),
                null,
                content,
                LocalDateTime.now(),
                "test",
                LocalDateTime.now(),
                "test"
        );
    }

    private ArticleCommentDto createArticleCommentDto(Long parentId, String content) {
        return ArticleCommentDto.of(
                1L,
                1L,
                createUserAccountDto(),
                parentId,
                content,
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