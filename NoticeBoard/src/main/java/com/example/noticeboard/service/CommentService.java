package com.example.noticeboard.service;

import com.example.noticeboard.domain.Article;
import com.example.noticeboard.domain.Comment;
import com.example.noticeboard.dto.ArticleDto;
import com.example.noticeboard.dto.CommentDto;
import com.example.noticeboard.repository.ArticleRepository;
import com.example.noticeboard.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
@Transactional
public class CommentService {
    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;

    @Transactional(readOnly = true)
    public List<CommentDto> searchComments(Long articleId) {
        Stream<Comment> comments = articleRepository.findById(articleId).orElseThrow()
                .getComments().stream();
        return comments.map(CommentDto::from).toList();
    }
}
