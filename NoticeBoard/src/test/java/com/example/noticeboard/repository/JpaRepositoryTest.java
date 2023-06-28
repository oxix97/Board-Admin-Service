package com.example.noticeboard.repository;

import com.example.noticeboard.domain.Article;
import com.example.noticeboard.domain.ArticleComment;
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
import static org.assertj.core.api.InstanceOfAssertFactories.COLLECTION;

@DisplayName("JPA 연결 테스트")
@Import(JpaRepositoryTest.TestJpaConfig.class)
@DataJpaTest
class JpaRepositoryTest {
    private final ArticleRepository articleRepository;
    private final ArticleCommentRepository articleCommentRepository;

    @Autowired
    public JpaRepositoryTest(ArticleRepository articleRepository, ArticleCommentRepository articleCommentRepository) {
        this.articleRepository = articleRepository;
        this.articleCommentRepository = articleCommentRepository;
    }

    @DisplayName("SELECT Test")
    @Test
    void selectTest() {
        List<Article> articles = articleRepository.findAll();
        assertThat(articles)
                .isNotNull();
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
        Long preCommentCount = articleCommentRepository.count();
        int deletedCommentSize = article.getArticleComments().size();

        articleRepository.delete(article);
    }

//    private void insertData() {
//        Article article = Article.of("dummy", "momo", "#dummy");
//        ArticleComment comment = ArticleComment.of(article, "i'm hungry");
//        articleRepository.save(article);
//        commentRepository.saveAndFlush(comment);
//    }

    @DisplayName("댓글에 대한 대댓글 조회 테스트")
    @Test
    void test111() {
        //given

        //when
        Optional<ArticleComment> parentComment = articleCommentRepository.findById(1L);

        //then
        assertThat(parentComment).get()
                .hasFieldOrPropertyWithValue("parentCommentId", null)
                .extracting("childComments", COLLECTION)
                .hasSize(4);
    }

    @DisplayName("댓글에 대댓글 삽입 테스트")
    @Test
    void test112() {
        //given
        ArticleComment parentArticleComment = articleCommentRepository.getReferenceById(1L);
        ArticleComment childArticleComment = ArticleComment.of(
                parentArticleComment.getArticle(),
                parentArticleComment.getUserAccount(),
                "대댓글"
        );

        //when
        parentArticleComment.addChildComment(childArticleComment);
        articleRepository.flush();

        //then
        assertThat(articleCommentRepository.findById(1L)).get()
                .hasFieldOrPropertyWithValue("parentCommentId", null)
                .extracting("childComments", COLLECTION)
                .hasSize(5);
    }

    @EnableJpaAuditing
    @TestConfiguration
    public static class TestJpaConfig {
        @Bean
        public AuditorAware<String> auditorAware() {
            return () -> Optional.of("test1");
        }
    }
}