package com.example.noticeboard.service;

import com.example.noticeboard.domain.type.SearchType;
import com.example.noticeboard.dto.ArticleDto;
import com.example.noticeboard.dto.ArticleUpdateDto;
import com.example.noticeboard.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.h2.mvstore.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class ArticleService {
    private final ArticleRepository repository;

    @Transactional(readOnly = true)
    public List<ArticleDto> searchArticles(SearchType title, String searchKeyword) {
        return List.of();
    }

    public void saveArticle(ArticleDto dto) {

    }

    public void updateArticle(long id, ArticleUpdateDto dto) {

    }

    public void deleteArticle(long id, ArticleUpdateDto dto) {

    }
}
