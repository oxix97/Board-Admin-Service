package com.example.noticeboard.service;

import com.example.noticeboard.domain.Article;
import com.example.noticeboard.domain.ArticleComment;
import com.example.noticeboard.domain.Hashtag;
import com.example.noticeboard.domain.UserAccount;
import com.example.noticeboard.dto.ArticleCommentDto;
import com.example.noticeboard.dto.UserAccountDto;
import com.example.noticeboard.dto.request.ArticleCommentRequest;
import com.example.noticeboard.dto.request.ArticleRequest;
import com.example.noticeboard.repository.ArticleRepository;
import com.example.noticeboard.repository.ArticleCommentRepository;
import com.example.noticeboard.repository.UserAccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;


@DisplayName("비즈니스 로직 - 댓글")
@ExtendWith(MockitoExtension.class)
class ArticleCommentServiceTest {
    @InjectMocks
    private ArticleCommentService articleCommentService;
    @Mock
    private ArticleCommentRepository articleCommentRepository;
    @Mock
    private ArticleRepository articleRepository;
    @Mock
    private UserAccountRepository userAccountRepository;

    @WithUserDetails(value = "chan", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("댓글 정보를 입력하면, 댓글을 저장.")
    @Test
    void test2() {
        //given
        ArticleCommentDto dto = createCommentDto("댓글");

        given(articleRepository.getReferenceById(dto.articleId())).willReturn(createArticle());
        given(userAccountRepository.getReferenceById(dto.userAccountDto().userId())).willReturn(createUserAccount());
        given(articleCommentRepository.save(any(ArticleComment.class))).willReturn(null);

        //when
        articleCommentService.saveComment(dto);

        //then
        then(articleRepository).should().getReferenceById(dto.articleId());
        then(userAccountRepository).should().getReferenceById(dto.userAccountDto().userId());
        then(articleCommentRepository).should().save(any());
    }

    @WithUserDetails(value = "chan", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("댓글 ID로 댓글 삭제")
    @Test
    void test3() {
        //given
        Long commentId = 1L;
        String userId = "chan";
        willDoNothing().given(articleCommentRepository).deleteByIdAndUserAccount_UserId(commentId, userId);

        //when
        articleCommentService.deleteComment(commentId, userId);

        //then
        then(articleCommentRepository).should().deleteByIdAndUserAccount_UserId(commentId, userId);
    }


    @DisplayName("부모 댓글 ID와 댓글 정보를 입력하면, 대댓글을 저장한다.")
    @Test
    void givenParentCommentIdAndArticleCommentInfo_whenSaving_thenSavesChildComment() {
        // Given
        Long parentCommentId = 1L;
        ArticleComment parent = createComment(parentCommentId, "댓글");
        ArticleCommentDto child = createCommentDto(parentCommentId, "대댓글");

        given(articleRepository.getReferenceById(child.articleId())).willReturn(createArticle());
        given(userAccountRepository.getReferenceById(child.userAccountDto().userId())).willReturn(createUserAccount());
        given(articleCommentRepository.getReferenceById(child.parentCommentId())).willReturn(parent);

        // When
        articleCommentService.saveComment(child);

        // Then
        assertThat(child.parentCommentId()).isNotNull();
        then(articleRepository).should().getReferenceById(child.articleId());
        then(userAccountRepository).should().getReferenceById(child.userAccountDto().userId());
        then(articleCommentRepository).should().getReferenceById(child.parentCommentId());
        then(articleCommentRepository).should(never()).save(any(ArticleComment.class));
    }


    private ArticleCommentDto createCommentDto(String content) {
        return createCommentDto(null, content);
    }

    private ArticleCommentDto createCommentDto(Long parentCommentId, String content) {
        return createCommentDto(1L, parentCommentId, content);
    }

    private ArticleCommentDto createCommentDto(Long id, Long parentCommentId, String content) {
        return ArticleCommentDto.of(
                id,
                1L,
                createUserAccountDto(),
                parentCommentId,
                content,
                LocalDateTime.now(),
                "uno",
                LocalDateTime.now(),
                "uno"
        );
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

    private ArticleComment createComment(Long id, String content) {
        ArticleComment articleComment = ArticleComment.of(
                createArticle(),
                createUserAccount(),
                content
        );
        ReflectionTestUtils.setField(articleComment, "id", id);

        return articleComment;
    }

    private UserAccount createUserAccount() {
        return UserAccount.of(
                "uno",
                "password",
                "uno@email.com",
                "Uno",
                null
        );
    }

    private Article createArticle() {
        Article article = Article.of(
                createUserAccount(),
                "title",
                "content"
        );
        ReflectionTestUtils.setField(article, "id", 1L);
        article.addHashtags(Set.of(createHashtag(article)));

        return article;
    }

    private Hashtag createHashtag(Article article) {
        return Hashtag.of("java");
    }
}