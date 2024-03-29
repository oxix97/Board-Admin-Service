package com.example.adminservice.controller;

import com.example.adminservice.config.GlobalControllerConfig;
import com.example.adminservice.config.TestSecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("[VIEW] 루트 컨트롤러")
@WebMvcTest(MainController.class)
@Import({TestSecurityConfig.class, GlobalControllerConfig.class})
class MainControllerTest {
    private final MockMvc mvc;

    public MainControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @WithMockUser(username = "test", roles = "USER")
    @DisplayName("[GET] 루트페이지 -> 게시글 관리 페이지 Fowarding")
    @Test
    void givenNothing_whenRequestingRootView_thenForwardsToManagementArticlesView() throws Exception{
        //given

        //when
        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("forward:/management/articles"))
                .andExpect(forwardedUrl("/management/articles"));

        //then
    }
}