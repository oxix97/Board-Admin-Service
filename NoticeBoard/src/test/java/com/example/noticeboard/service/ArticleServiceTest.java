package com.example.noticeboard.service;

import com.example.noticeboard.domain.Article;
import com.example.noticeboard.domain.type.SearchType;
import com.example.noticeboard.dto.ArticleDto;
import com.example.noticeboard.dto.ArticleUpdateDto;
import com.example.noticeboard.repository.ArticleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

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
        List<ArticleDto> articles = service.searchArticles(SearchType.TITLE, "search keyword");
        assertThat(articles)
                .isNotNull();
    }

    @DisplayName("[GET] 아이디로 게시글 조회시 게시글 반환")
    @Test
    void test2() {

    }

    @DisplayName("[PUT] 게시글 정보 입력시 게시글 생성")
    @Test
    void test3() {
        ArticleDto dto = ArticleDto.of("title", "content", "#Test3", LocalDateTime.now(), "Chan");
        given(repository.save(any(Article.class))).willReturn(null);
        service.saveArticle(dto);
        then(repository).should().save(any(Article.class));
    }

    @DisplayName("[PUT] 게시글의 ID와 수정 정보를 입력 -> 게시글 수정")
    @Test
    void test4() {
        ArticleUpdateDto dto = ArticleUpdateDto.of("title", "content", "#Test3");
        given(repository.save(any(Article.class))).willReturn(null);
        service.updateArticle(1L, dto);
        then(repository).should().save(any(Article.class));
    }

    @DisplayName("[DELETE] 게시글의 ID를 가지고 게시글 삭제")
    @Test
    void test5() {
        ArticleUpdateDto dto = ArticleUpdateDto.of("title", "content", "#Test3");
        willDoNothing().given(repository).delete(any(Article.class));
        service.deleteArticle(1L, dto);
        then(repository).should().delete(any(Article.class));
    }
}