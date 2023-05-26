package com.example.noticeboard.repository;

import com.example.noticeboard.config.JpaConfig;
import com.example.noticeboard.domain.Article;
import com.example.noticeboard.domain.Comment;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled
@DisplayName("JPA 연결 테스트")
@Import(JpaRepositoryTest.TestJpaConfig.class)
@DataJpaTest
class JpaRepositoryTest {
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public JpaRepositoryTest(ArticleRepository articleRepository, CommentRepository commentRepository) {
        this.articleRepository = articleRepository;
        this.commentRepository = commentRepository;
    }

    @DisplayName("SELECT Test")
    @Test
    void selectTest() {
        List<Article> articles = articleRepository.findAll();
        assertThat(articles)
                .isNotNull()
                .hasSize(0);

    }

//    @DisplayName("INSERT Test")
//    @Test
//    void insertTest() {
//        Article article = Article.of("new article",
//                "content",
//                "#spring");
//
//        articleRepository.save(article);
//        int count = articleRepository.findAll().size();
//
//        Assertions.assertEquals(1, count);
//
//    }

//    @DisplayName("UPDATE Test")
//    @Test
//    void updateTest() {
//        insertData();
//        Article article = articleRepository.findById(1L).orElseThrow();
//        String updateHashtag = "#update";
//        article.setHashtag(updateHashtag);
//        articleRepository.save(article);
//        assertThat(article).hasFieldOrPropertyWithValue("hashtag", updateHashtag);
//    }

    @DisplayName("DELETE Test")
    @Test
    void deleteTest() {
//        insertData();
        Article article = articleRepository.findById(1L).orElseThrow();
        Long preArticleCount = articleRepository.count();
        Long preCommentCount = commentRepository.count();
        int deletedCommentSize = article.getComments().size();

        articleRepository.delete(article);
    }

//    private void insertData() {
//        Article article = Article.of("dummy", "momo", "#dummy");
//        Comment comment = Comment.of(article, "i'm hungry");
//        articleRepository.save(article);
//        commentRepository.saveAndFlush(comment);
//    }

    @EnableJpaAuditing
    @TestConfiguration
    public static class TestJpaConfig {
        @Bean
        public AuditorAware<String> auditorAware() {
            return () -> Optional.of("test1");
        }
    }
}