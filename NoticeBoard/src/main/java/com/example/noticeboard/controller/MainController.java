package com.example.noticeboard.controller;

import com.example.noticeboard.dto.response.ArticleCommentResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;

@Controller
public class MainController {
    @GetMapping("/")
    public String root() {
        return "redirect:/articles";
    }

    @ResponseBody
    @GetMapping("/test-rest")
    public ArticleCommentResponse test(Long id) {
        return ArticleCommentResponse.of(
                id,
                "content",
                LocalDateTime.now(),
                "email@naver.com",
                "chan",
                "chan"
        );
    }
}
