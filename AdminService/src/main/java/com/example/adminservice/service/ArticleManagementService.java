package com.example.adminservice.service;

import com.example.adminservice.dto.ArticleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ArticleManagementService {
    //todo 게시판 조회
    public List<ArticleDto> getArticles() {
        return List.of();
    }

    // todo 게시글 단건 조회
    public ArticleDto getArticle(Long id) {
        return null;
    }

    // todo 게시글 삭제
    public void deleteArticle(Long id) {

    }
}
