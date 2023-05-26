package com.example.noticeboard.service;

import com.example.noticeboard.domain.Article;
import com.example.noticeboard.domain.UserAccount;
import com.example.noticeboard.domain.type.SearchType;
import com.example.noticeboard.dto.*;
import com.example.noticeboard.repository.ArticleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@DisplayName("비즈니스 로직 - 게시판,게시글")
@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    @InjectMocks
    private ArticleService service;
    @Mock
    private ArticleRepository repository;

    @DisplayName("[GET] 검색시 해당되는 게시글들 반환")
    @Test
    void test1() {
        Pageable pageable = Pageable.ofSize(20);
        given(repository.findAll(pageable)).willReturn(Page.empty());
        Page<ArticleDto> articles = service.searchArticles(null, null, pageable);

        assertThat(articles)
                .isEmpty();
        then(repository).should().findAll(pageable);
    }

    @DisplayName("[GET] 검색어 없이 게시글 해시태그 검색  -> 빈 페이지 반환")
    @Test
    void test11() {
        Pageable pageable = Pageable.ofSize(20);

        // When
        Page<ArticleDto> articles = service.searchArticlesViaHashtag(null, pageable);

        // Then
        assertThat(articles).isEqualTo(Page.empty(pageable));
        then(repository).shouldHaveNoInteractions();
    }

    @DisplayName("[GET] 해시태그 검색 -> 게시글 페이지 반환")
    @Test
    void test12() {
        String hashtag = "#java";
        Pageable pageable = Pageable.ofSize(20);
        given(repository.findByHashtagNames(List.of(hashtag), pageable)).willReturn(Page.empty(pageable));

        // When
        Page<ArticleDto> articles = service.searchArticlesViaHashtag(hashtag, pageable);

        // Then
        assertThat(articles).isEmpty();
        then(repository).should().findByHashtagNames(List.of(hashtag), pageable);
    }

    @DisplayName("[GET] 해시태그 조회시, 해시태그 리스트 반환")
    @Test
    void test13() {
        //given
        List<String> expectedHashtags = List.of("#java", "#spring", "#boot");
        given(repository.findAllDistinctHashtags()).willReturn(expectedHashtags);

        //when
        List<String> actualHashtags = service.getHashtags();

        //then
        assertThat(actualHashtags).isEqualTo(expectedHashtags);
        then(repository).should().findAllDistinctHashtags();
    }

    @DisplayName("[GET] 아이디로 게시글 조회시 게시글 반환")
    @Test
    void test2() {
        Long articleId = 1L;
        Article article = createArticle();
        given(repository.findById(articleId)).willReturn(Optional.of(article));

        ArticleWithCommentDto dto = service.getArticleWithComments(articleId);

        assertThat(dto)
                .hasFieldOrPropertyWithValue("title", article.getTitle())
                .hasFieldOrPropertyWithValue("content", article.getContent())
                .hasFieldOrPropertyWithValue("hashtags", article.getHashtags());
        then(repository).should().findById(articleId);
    }

    @DisplayName("[PUT] 게시글 정보 입력시 게시글 생성")
    @Test
    void test3() {
        /*ArticleDto dto = ArticleDto.of("title", "content", "#Test3", LocalDateTime.now(), "Chan");
        given(repository.save(any(Article.class))).willReturn(null);
        service.saveArticle(dto);
        then(repository).should().save(any(Article.class));*/
    }

    @DisplayName("[PUT] 게시글의 ID와 수정 정보를 입력 -> 게시글 수정")
    @Test
    void test4() {
        long articleId = 1L;
        Article article = createArticle();
        ArticleDto dto = createArticleDto("새 타이틀", "새 내용");

        //given
        given(repository.getReferenceById(dto.id())).willReturn(article);

        //when
        service.updateArticle(articleId, dto);

        //then
        assertThat(article)
                .hasFieldOrPropertyWithValue("title", dto.title())
                .hasFieldOrPropertyWithValue("content", dto.content())
                .hasFieldOrPropertyWithValue("hashtags", dto.hashtagDtos().stream().map(HashtagDto::hashtagName));
        then(repository).should().getReferenceById(dto.id());
    }

    @DisplayName("[DELETE] 게시글의 ID를 가지고 게시글 삭제")
    @Test
    void test5() {
//        long articleId = 1L;
//        Article article = createArticle();
//        ArticleDto dto = createArticleDto();
//        given(repository.getReferenceById(dto.id())).willReturn(article);
//        service.deleteArticle(dto.id());
//        willDoNothing().given(repository).deleteById(articleId);
    }

    private Article createArticle() {
        return createArticle(1L);
    }

    private Article createArticle(Long id) {
        Article article = Article.of(
                createUserAccount(),
                "title",
                "content"
        );
        ReflectionTestUtils.setField(article, "id", id);

        return article;
    }

    private UserAccount createUserAccount() {
        return createUserAccount("uno");
    }

    private UserAccount createUserAccount(String userId) {
        return UserAccount.of(
                userId,
                "password",
                "uno@email.com",
                "Uno",
                null
        );
    }

    private ArticleDto createArticleDto() {
        return createArticleDto("title", "content");
    }

    private ArticleDto createArticleDto(String title, String content) {
        return ArticleDto.of(
                1L,
                createUserAccountDto(),
                title,
                content,
                null,
                LocalDateTime.now(),
                "Chan",
                LocalDateTime.now(),
                "Chan");
    }

    private UserAccountDto createUserAccountDto() {
        return UserAccountDto.of(
                "uno",
                "password",
                "uno@mail.com",
                "Uno",
                "This is memo",
                LocalDateTime.now(),
                "uno",
                LocalDateTime.now(),
                "uno"
        );
    }
}