package com.example.adminservice.service;

import com.example.adminservice.dto.ArticleDto;
import com.example.adminservice.dto.properties.ProjectProperties;
import com.example.adminservice.dto.response.ArticleClientResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ArticleManagementService {

    private final RestTemplate template;
    private final ProjectProperties properties;

    //todo 게시판 조회
    public List<ArticleDto> getArticles() {
        URI uri = UriComponentsBuilder
                .fromHttpUrl(properties.board().url() + "/api/articles")
                .queryParam("size", 10000)
                .build()
                .toUri();

        ArticleClientResponse response = template.getForObject(uri, ArticleClientResponse.class);

        return Optional.ofNullable(response)
                .orElseGet(ArticleClientResponse::empty).articles();
    }

    // todo 게시글 단건 조회
    public ArticleDto getArticle(Long id) {
        URI uri = UriComponentsBuilder
                .fromHttpUrl(properties.board().url() + "/api/articles/" + id)
                .queryParam("projection","withUserAccount")
                .build()
                .toUri();

        ArticleDto response = template.getForObject(uri, ArticleDto.class);

        return Optional.ofNullable(response)
                .orElseThrow(() -> new NoSuchElementException("게시글을 찾을 수 없습니다 article-id : " + id));
    }

    // todo 게시글 삭제
    public void deleteArticle(Long id) {
        URI uri = UriComponentsBuilder
                .fromHttpUrl(properties.board().url() + "/api/articles/" + id + "/delete")
                .build()
                .toUri();

        template.delete(uri);
    }
}
