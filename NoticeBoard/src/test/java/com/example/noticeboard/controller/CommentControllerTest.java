package com.example.noticeboard.controller;

import com.example.noticeboard.config.SecurityConfig;
import com.example.noticeboard.dto.CommentDto;
import com.example.noticeboard.dto.request.CommentRequest;
import com.example.noticeboard.service.CommentService;
import com.example.noticeboard.util.FormDataEncoder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import({SecurityConfig.class, FormDataEncoder.class})
@DisplayName("View Controller - 댓글")
@WebMvcTest(CommentController.class)
class CommentControllerTest {
    private final MockMvc mvc;
    private final FormDataEncoder formDataEncoder;

    @MockBean
    private CommentService commentService;

    public CommentControllerTest(
            @Autowired MockMvc mvc,
            @Autowired FormDataEncoder formDataEncoder
    ) {
        this.mvc = mvc;
        this.formDataEncoder = formDataEncoder;
    }

    @DisplayName("[POST] 댓글 등록 - 정상 호출")
    @Test
    void test1() throws Exception {
        //given
        long articleId = 1L;
        CommentRequest request = CommentRequest.of(articleId, "comment-content");
        willDoNothing().given(commentService).saveComment(any(CommentDto.class));

        //when
        mvc.perform(post("/comments/new")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(formDataEncoder.encode(request))
                        .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(view().name("redirect:/articles/" + articleId))
                .andExpect(redirectedUrl("/articles/" + articleId));

        //then
        then(commentService).should().saveComment(any(CommentDto.class));
    }

    @DisplayName("[DELETE] 댓글 삭제 - 정상 호출")
    @Test
    void test2() throws Exception {
        //given
        long articleId = 1L;
        long commentId = 1L;
        willDoNothing().given(commentService).deleteComment(commentId);

        //when
        mvc.perform(delete("/comments/" + commentId + "/delete")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(formDataEncoder.encode(Map.of("articleId", articleId)))
                        .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(view().name("redirect:/articles/" + articleId))
                .andExpect(redirectedUrl("/articles/" + articleId));

        //then
        then(commentService).should().deleteComment(commentId);
    }
}