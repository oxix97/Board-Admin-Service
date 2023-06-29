package com.example.noticeboard.controller;

import com.example.noticeboard.config.TestSecurityConfig;
import com.example.noticeboard.domain.type.FormStatus;
import com.example.noticeboard.domain.type.SearchType;
import com.example.noticeboard.dto.ArticleDto;
import com.example.noticeboard.dto.ArticleWithCommentDto;
import com.example.noticeboard.dto.HashtagDto;
import com.example.noticeboard.dto.UserAccountDto;
import com.example.noticeboard.dto.request.ArticleRequest;
import com.example.noticeboard.dto.response.ArticleResponse;
import com.example.noticeboard.service.ArticleService;
import com.example.noticeboard.service.PaginationService;
import com.example.noticeboard.util.FormDataEncoder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import({TestSecurityConfig.class, FormDataEncoder.class})
@DisplayName("View Controller - 게시글")
@WebMvcTest(ArticleController.class)
class ArticleControllerTest {
    private final MockMvc mvc;

    private final FormDataEncoder formDataEncoder;

    @MockBean
    private ArticleService articleService;

    @MockBean
    private PaginationService paginationService;

    public ArticleControllerTest(
            @Autowired MockMvc mvc,
            @Autowired FormDataEncoder formDataEncoder) {
        this.mvc = mvc;
        this.formDataEncoder = formDataEncoder;
    }

    @DisplayName("[GET] 게시글 리스트 (인증된 사용자)- 정상 호출")
    @Test
    void test1() throws Exception {
        //given
        given(articleService.searchArticles(eq(null), eq(null), any(Pageable.class))).willReturn(Page.empty());

        //when & then
        mvc.perform(get("/articles"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/index"))
                .andExpect(model().attributeExists("articles"));
//                .andExpect(model().attributeExists("searchTypes"));

        then(articleService).should().searchArticles(eq(null), eq(null), any(Pageable.class));
    }

    // @WithMockUser todo Security 인증 통과시키는 어노테이션
    //todo 테스트 유저를 가지고 인증
    @WithUserDetails(value = "security-test", setupBefore = TestExecutionEvent.TEST_EXECUTION, userDetailsServiceBeanName = "userDetailsService")
    @DisplayName("[GET] 게시글 상세페이지 (인증된 사용자)- 정상 호출")
    @Test
    void test3() throws Exception {
        Long id = 1L;
        long totalCount = 1L;
        //given
        given(articleService.getArticleWithComments(id)).willReturn(createArticleWithCommentsDto());
        //when
        mvc.perform(get("/articles/" + id))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/detail"))
                .andExpect(model().attributeExists("article"))
                .andExpect(model().attributeExists("articleComments"));

        //then
        then(articleService).should().getArticleWithComments(id);

    }

    @DisplayName("[GET] 게시글 상세페이지 (인증되지 않은 사용자 -> 로그인 페이지 이동)")
    @Test
    void test31() throws Exception {
        //given
        long articleId = 1L;
        //when
        mvc.perform(get("/articles/" + articleId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
        //then
        then(articleService).shouldHaveNoInteractions();
        then(articleService).shouldHaveNoInteractions();
    }

    @DisplayName("[GET] 게시글 검색 전용 페이지 - 정상 호출")
    @Test
    void test4() throws Exception {
        SearchType searchType = SearchType.TITLE;
        String searchValue = "title";

        //given
        given(articleService.searchArticles(eq(searchType), eq(searchValue), any(Pageable.class))).willReturn(Page.empty());
        given(paginationService.getPaginationBarNumbers(anyInt(), anyInt())).willReturn(List.of(0, 1, 2, 3, 4));

        //when
        mvc.perform(get("/articles")
                        .queryParam("searchType", searchType.name())
                        .queryParam("searchValue", searchValue))
                .andExpect(status().isOk())
                .andExpect(view().name("articles/index"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(model().attributeExists("searchTypes"))
                .andExpect(model().attributeExists("articles"));

        //then
        then(articleService).should().searchArticles(eq(searchType), eq(searchValue), any(Pageable.class));
        then(paginationService).should().getPaginationBarNumbers(anyInt(), anyInt());
    }

    @DisplayName("[GET] 게시글 해시태그 검색 페이지 (해시태그 없는) - 정상 호출")
    @Test
    void test5() throws Exception {
        //given
        List<String> hashtags = List.of("#java", "#spring", "#boot");
        given(articleService.searchArticlesViaHashtag(eq(null), any(Pageable.class))).willReturn(Page.empty());
        given(paginationService.getPaginationBarNumbers(anyInt(), anyInt())).willReturn(List.of(1, 2, 3, 4, 5));
        given(articleService.getHashtags()).willReturn(hashtags);

        //when
        mvc.perform(get("/articles/search-hashtag"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/search-hashtag"))
                .andExpect(model().attributeExists("articles"))
                .andExpect(model().attribute("hashtags", hashtags))
                .andExpect(model().attribute("searchType", SearchType.HASHTAG))
                .andExpect(model().attributeExists("paginationBarNumbers"));

        //then
        then(articleService).should().searchArticlesViaHashtag(eq(null), any(Pageable.class));
        then(articleService).should().getHashtags();
        then(paginationService).should().getPaginationBarNumbers(anyInt(), anyInt());
    }

    @DisplayName("[GET] 게시글 해시태그 검색 페이지 (해시태그 있는) - 정상 호출")
    @Test
    void test6() throws Exception {
        //given
        String hashtag = "#java";
        List<String> hashtags = List.of("#java", "#spring", "#boot");
        given(articleService.searchArticlesViaHashtag(eq(hashtag), any(Pageable.class))).willReturn(Page.empty());
        given(paginationService.getPaginationBarNumbers(anyInt(), anyInt())).willReturn(List.of(1, 2, 3, 4, 5));
        given(articleService.getHashtags()).willReturn(hashtags);

        //when
        mvc.perform(get("/articles/search-hashtag")
                        .queryParam("searchValue", hashtag))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/search-hashtag"))
                .andExpect(model().attribute("articles", Page.empty()))
                .andExpect(model().attribute("hashtags", hashtags))
                .andExpect(model().attribute("searchType", SearchType.HASHTAG))
                .andExpect(model().attributeExists("paginationBarNumbers"));

        //then
        then(articleService).should().searchArticlesViaHashtag(eq(hashtag), any(Pageable.class));
        then(articleService).should().getHashtags();
        then(paginationService).should().getPaginationBarNumbers(anyInt(), anyInt());
    }

    @WithUserDetails(value = "security-test", setupBefore = TestExecutionEvent.TEST_EXECUTION, userDetailsServiceBeanName = "userDetailsService")
    @DisplayName("[DELETE] 게시글 삭제 - 정상 호출")
    @Test
    void test41() throws Exception {
        //given
        long articleId = 1L;
        String userId = "test";
        willDoNothing().given(articleService).deleteArticle(articleId, userId);
        //when
        mvc.perform(post("/articles/" + articleId + "/delete")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/articles"));
        //then
        then(articleService).should().deleteArticle(articleId, userId);
    }

    @WithMockUser
    @DisplayName("[GET] 게시글 작성 페이지 - 정상 호출")
    @Test
    void test2() throws Exception {
        //given

        //when
        mvc.perform(get("/articles/form"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("formStatus", FormStatus.CREATE))
                .andExpect(view().name("articles/form"));

        //then
    }

    @DisplayName("[PUT] 게시글 작성 - 정상 호출")
    @WithUserDetails(value = "security-test", setupBefore = TestExecutionEvent.TEST_EXECUTION, userDetailsServiceBeanName = "userDetailsService")
    @Test
    void test21() throws Exception {
        //given
        ArticleDto article = createArticleDto();
        ArticleRequest request = ArticleRequest.of("title21", "content21");
        willDoNothing().given(articleService).saveArticle(article);

        //when
        mvc.perform(post("/articles/form")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(formDataEncoder.encode(request))
                        .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/articles"))
                .andExpect(redirectedUrl("/articles"));
        //then
        then(articleService).should().saveArticle(any(ArticleDto.class));
    }

    @DisplayName("[GET] 게시글 수정 페이지 - 정상 호출")
    @WithUserDetails(value = "security-test", setupBefore = TestExecutionEvent.TEST_EXECUTION, userDetailsServiceBeanName = "userDetailsService")
    @Test
    void test22() throws Exception {
        //given
        long articleId = 1L;
        ArticleDto article = createArticleDto();
        given(articleService.getArticle(articleId)).willReturn(article);
        //when
        mvc.perform(get("/articles/form/" + articleId))
                .andExpect(status().isOk())
                .andExpect(view().name("articles/form"))
                .andExpect(model().attribute("article", ArticleResponse.from(article)))
                .andExpect(model().attribute("formStatus", FormStatus.UPDATE));

        //then
        then(articleService).should().getArticle(articleId);
    }

    private ArticleDto createArticleDto() {
        return ArticleDto.of(
                createUserAccountDto(),
                "title",
                "content",
                Set.of(HashtagDto.of("java"))
        );
    }

    private ArticleWithCommentDto createArticleWithCommentsDto() {
        return ArticleWithCommentDto.of(
                1L,
                createUserAccountDto(),
                Set.of(),
                "title",
                "content",
                Set.of(HashtagDto.of("java")),
                LocalDateTime.now(),
                "uno",
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