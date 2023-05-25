package com.example.noticeboard.service;

import com.example.noticeboard.domain.Article;
import com.example.noticeboard.domain.Comment;
import com.example.noticeboard.domain.Hashtag;
import com.example.noticeboard.domain.UserAccount;
import com.example.noticeboard.dto.CommentDto;
import com.example.noticeboard.dto.UserAccountDto;
import com.example.noticeboard.repository.ArticleRepository;
import com.example.noticeboard.repository.CommentRepository;
import com.example.noticeboard.repository.UserAccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;


@DisplayName("비즈니스 로직 - 댓글")
@ExtendWith(MockitoExtension.class)
class CommentServiceTest {
    @InjectMocks
    private CommentService commentService;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private ArticleRepository articleRepository;
    @Mock
    private UserAccountRepository userAccountRepository;

    @DisplayName("[POST] 댓글 정보를 입력하면, 댓글을 저장.")
    @Test
    void test2() {
        //given
        CommentDto dto = createCommentDto("댓글");

        given(articleRepository.getReferenceById(dto.articleId())).willReturn(createArticle());
        given(userAccountRepository.getReferenceById(dto.userAccountDto().userId())).willReturn(createUserAccount());
        given(commentRepository.save(any(Comment.class))).willReturn(null);

        //when
        commentService.saveComment(dto);

        //then
        then(articleRepository).should().getReferenceById(dto.articleId());
        then(userAccountRepository).should().getReferenceById(dto.userAccountDto().userId());
        then(commentRepository).should().save(any());
    }

    @DisplayName("[DELETE] 댓글 ID로 댓글 삭제")
    @Test
    void test3() {
        //given
        Long commentId = 1L;
        willDoNothing().given(commentRepository).deleteById(commentId);

        //when
        commentService.deleteComment(commentId);

        //then
        then(commentRepository).should().deleteById(commentId);
    }

    private CommentDto createCommentDto(String content) {
        return createCommentDto(null, content);
    }

    private CommentDto createCommentDto(Long parentCommentId, String content) {
        return createCommentDto(1L, parentCommentId, content);
    }

    private CommentDto createCommentDto(Long id, Long parentCommentId, String content) {
        return CommentDto.of(
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

    private Comment createComment(Long id, String content) {
        Comment comment = Comment.of(
                createArticle(),
                createUserAccount(),
                content
        );
        ReflectionTestUtils.setField(comment, "id", id);

        return comment;
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