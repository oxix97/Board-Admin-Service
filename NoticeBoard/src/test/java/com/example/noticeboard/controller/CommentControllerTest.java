package com.example.noticeboard.controller;

import com.example.noticeboard.config.SecurityConfig;
import com.example.noticeboard.repository.CommentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

@Import(SecurityConfig.class)
@DisplayName("View Controller - 게시글")
@WebMvcTest(CommentController.class)
class CommentControllerTest {
    private final MockMvc mvc;

    public CommentControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @DisplayName("")
    @Test
    void test1(){

    }
}