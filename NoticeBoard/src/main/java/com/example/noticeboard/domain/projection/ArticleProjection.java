package com.example.noticeboard.domain.projection;

import com.example.noticeboard.domain.Article;
import com.example.noticeboard.domain.UserAccount;
import org.springframework.data.rest.core.config.Projection;

import java.time.LocalDateTime;

@Projection(name = "withUserAccount", types = Article.class)
public interface ArticleProjection {
    Long getId();

    UserAccount getUserAccount();

    String getTitle();

    String getContent();

    LocalDateTime getCreatedAt();

    String getCreatedBy();

    LocalDateTime getModifiedAt();

    String getModifiedBy();
}
