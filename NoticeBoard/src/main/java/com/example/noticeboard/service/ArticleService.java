package com.example.noticeboard.service;

import com.example.noticeboard.domain.Article;
import com.example.noticeboard.domain.type.SearchType;
import com.example.noticeboard.dto.ArticleDto;
import com.example.noticeboard.dto.ArticleUpdateDto;
import com.example.noticeboard.dto.ArticleWithCommentDto;
import com.example.noticeboard.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class ArticleService {
    private final ArticleRepository repository;

    @Transactional(readOnly = true)
    public Page<ArticleDto> searchArticles(SearchType searchType, String searchKeyword, Pageable pageable) {
        if (searchKeyword == null || searchKeyword.isBlank()) {
            return repository.findAll(pageable).map(ArticleDto::from);
        }
        return switch (searchType) {
            case TITLE -> repository.findByTitleContaining(searchKeyword, pageable).map(ArticleDto::from);
            case CONTENT -> repository.findByContentContaining(searchKeyword, pageable).map(ArticleDto::from);
            case ID -> repository.findByUserAccount_UserIdContaining(searchKeyword, pageable).map(ArticleDto::from);
            case NICKNAME ->
                    repository.findByUserAccount_NicknameContaining(searchKeyword, pageable).map(ArticleDto::from);
            case HASHTAG -> repository.findByHashtag("#" + searchKeyword, pageable).map(ArticleDto::from);
        };
    }

    @Transactional(readOnly = true)
    public ArticleWithCommentDto getArticle(Long articleId) {
        return repository.findById(articleId)
                .map(ArticleWithCommentDto::from)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 없습니다. - articleId : " + articleId));
    }

    public void saveArticle(ArticleDto dto) {
        repository.save(dto.toEntity());
    }

    public void updateArticle(ArticleDto dto) {
        try {
            Article article = repository.getReferenceById(dto.id());
            if (dto.title() != null) article.setTitle(dto.title());
            if (dto.content() != null) article.setContent(dto.content());
            article.setHashtag(dto.hashtag());
        } catch (EntityNotFoundException e) {
            log.info("게시글 업데이트 실패. 게시글을 찾을 수 없습니다. - dto : {}", dto);
        }
    }

    public void deleteArticle(long articleId) {
        repository.deleteById(articleId);
    }
}
