package com.example.noticeboard.service;

import com.example.noticeboard.domain.Article;
import com.example.noticeboard.domain.Comment;
import com.example.noticeboard.domain.UserAccount;
import com.example.noticeboard.dto.ArticleDto;
import com.example.noticeboard.dto.CommentDto;
import com.example.noticeboard.repository.ArticleRepository;
import com.example.noticeboard.repository.CommentRepository;
import com.example.noticeboard.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class CommentService {
    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final UserAccountRepository userAccountRepository;

    @Transactional(readOnly = true)
    public List<CommentDto> searchComments(Long articleId) {
        Set<Comment> comments = articleRepository.findById(articleId).orElseThrow().getComments();

        return comments.stream().map(CommentDto::from).toList();
    }

    public void saveComment(CommentDto dto) {
        try {
            Article article = articleRepository.getReferenceById(dto.articleId());
            UserAccount userAccount = userAccountRepository.getReferenceById(dto.userAccountDto().userId());
            commentRepository.save(dto.toEntity(article, userAccount, dto.content()));
        } catch (EntityNotFoundException e) {
            log.warn("댓글 저장 실패. 댓글 작성에 필요한 정보를 찾을 수 없습니다. - {}", e.getLocalizedMessage());
        }
    }

    public void deleteComment(long commentId) {
        commentRepository.deleteById(commentId);
    }
}
