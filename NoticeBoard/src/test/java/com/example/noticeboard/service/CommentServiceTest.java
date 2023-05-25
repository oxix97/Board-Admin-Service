package com.example.noticeboard.service;

import com.example.noticeboard.domain.Article;
import com.example.noticeboard.domain.Comment;
import com.example.noticeboard.dto.CommentDto;
import com.example.noticeboard.repository.ArticleRepository;
import com.example.noticeboard.repository.CommentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.then;


@DisplayName("비즈니스 로직 - 댓글")
@ExtendWith(MockitoExtension.class)
class CommentServiceTest {
    @InjectMocks
    private CommentService commentService;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private ArticleRepository articleRepository;

//    @DisplayName("[GET] 해당하는 게시글의 댓글 리스트를 조회.")
//    @Test
//    void test1() {
//        //given
//        Long id = 1L;
//        Article article = Article.of("title", "content", "#test1");
//        Comment expected = Comment.of(article, "Comment11111");
//        BDDMockito.given(articleRepository.findById(id)).willReturn(Optional.of(article));
//        //when
//        List<CommentDto> actual = commentService.searchComments(id);
//
//        //then
//        assertThat(actual)
//                .hasSize(1)
//                .first().hasFieldOrPropertyWithValue("content", expected.getContent());
//        then(commentRepository).should().findById(id);
//    }

    @DisplayName("[PUT] 댓글 정보를 입력하면, 댓글을 저장.")
    @Test
    void test2() {
        //given

        //when

        //then
    }
}