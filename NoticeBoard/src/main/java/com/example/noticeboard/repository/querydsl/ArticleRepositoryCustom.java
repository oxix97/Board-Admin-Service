package com.example.noticeboard.repository.querydsl;

import com.example.noticeboard.domain.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public interface ArticleRepositoryCustom {
    List<String> findAllDistinctHashtags();
    Page<Article> findByHashtagNames(Collection<String> hashtagNames, Pageable pageable);
}
