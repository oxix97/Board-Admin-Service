package com.example.adminservice.service;

import com.example.adminservice.dto.ArticleCommentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ArticleCommentManagementService {

    public List<ArticleCommentDto> getArticleComments() {
        return null;
    }

    public ArticleCommentDto getArticleComment(Long commentId) {
        return null;
    }

    public void deleteArticleComment(Long id) {

    }
}
