package com.example.adminservice.service;

import com.example.adminservice.dto.ArticleCommentDto;
import com.example.adminservice.dto.ArticleDto;
import com.example.adminservice.dto.properties.ProjectProperties;
import com.example.adminservice.dto.response.ArticleClientResponse;
import com.example.adminservice.dto.response.ArticleCommentClientResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ArticleCommentManagementService {

    private final RestTemplate template;
    private final ProjectProperties properties;

    public List<ArticleCommentDto> getArticleComments() {
        URI uri = UriComponentsBuilder.fromHttpUrl(properties.board().url() + "/api/articleComments")
                .queryParam("size", 10000)
                .build()
                .toUri();
        ArticleCommentClientResponse response = template.getForObject(uri, ArticleCommentClientResponse.class);
        return Optional.ofNullable(response)
                .orElseGet(ArticleCommentClientResponse::empty)
                .articleComments();
    }

    public ArticleCommentDto getArticleComment(Long id) {
        URI uri = UriComponentsBuilder.fromHttpUrl(properties.board().url() + "/api/articleComments/" + id)
                .build()
                .toUri();
        ArticleCommentDto response = template.getForObject(uri, ArticleCommentDto.class);
        return Optional.ofNullable(response).orElseThrow(() -> new NoSuchElementException("댓글을 찾을 수 없습니다. commentId : " + id));
    }

    public void deleteArticleComment(Long id) {
        URI uri = UriComponentsBuilder.fromHttpUrl(properties.board().url() + "/api/articleComments/" + id+"/delete")
                .build()
                .toUri();

        template.delete(uri);
    }
}
