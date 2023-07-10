package com.example.adminservice.controller;

import com.example.adminservice.config.SecurityConfig;
import com.example.adminservice.config.TestSecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("[VIEW] 어드민 관리")
@Import(TestSecurityConfig.class)
@WebMvcTest(AdminAccountController.class)
class AdminAccountControllerTest {
    private final MockMvc mvc;

    public AdminAccountControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @DisplayName("[GET] 게시글 관리 페이지 - 정상 호출")
    @Test
    void givenNothing_whenRequestingArticleManagementView_thenReturnView() throws Exception {
        //given

        //when
        mvc.perform(get("/admin/members"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("admin/members"));

        //then

    }
}