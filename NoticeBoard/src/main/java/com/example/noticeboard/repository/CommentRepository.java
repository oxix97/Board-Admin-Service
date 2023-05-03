package com.example.noticeboard.repository;

import com.example.noticeboard.domain.Comment;
import com.example.noticeboard.domain.QArticle;
import com.example.noticeboard.domain.QComment;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource
public interface CommentRepository
        extends JpaRepository<Comment, Long>,
        QuerydslPredicateExecutor<Comment>,
        QuerydslBinderCustomizer<QComment> {

    List<Comment> findByArticle_Id(Long articleId);

    @Override
    default void customize(QuerydslBindings bindings, QComment root) {
        //todo QuerydslPredicateExecutor를 통해 모든 field들에 대한 검색이 가능하다.
        // 이러한 내용을 custom하기 위한 메서드
        bindings.excludeUnlistedProperties(true);
        bindings.including(root.content, root.createdAt, root.createdBy);
        bindings.bind(root.content).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.createdAt).first(DateTimeExpression::eq);
        bindings.bind(root.createdBy).first(StringExpression::containsIgnoreCase);
    }
}