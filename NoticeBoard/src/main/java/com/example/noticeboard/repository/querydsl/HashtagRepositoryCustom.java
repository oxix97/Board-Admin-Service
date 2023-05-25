package com.example.noticeboard.repository.querydsl;

import com.example.noticeboard.domain.Hashtag;

import java.util.List;

public interface HashtagRepositoryCustom {
    List<String> findAllHashtagNames();
}
