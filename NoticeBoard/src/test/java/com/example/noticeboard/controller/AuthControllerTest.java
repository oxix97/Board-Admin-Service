package com.example.noticeboard.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Auth Controller - 인증")
@WebMvcTest
public class AuthControllerTest {
    private final MockMvc mvc;

    public AuthControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @WithMockUser
    @DisplayName("[GET] : 로그인페이지 - 정상 호출")
    @Test
    void test1() throws Exception {
        mvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML));
    }
}
