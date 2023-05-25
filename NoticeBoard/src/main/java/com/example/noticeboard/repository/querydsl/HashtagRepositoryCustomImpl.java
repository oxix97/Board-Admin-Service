package com.example.noticeboard.repository.querydsl;

import com.example.noticeboard.domain.Hashtag;
import com.example.noticeboard.domain.QHashtag;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class HashtagRepositoryCustomImpl extends QuerydslRepositorySupport implements HashtagRepositoryCustom {

    public HashtagRepositoryCustomImpl(Class<?> domainClass) {
        super(domainClass);
    }

    @Override
    public List<String> findAllHashtagNames() {
        QHashtag hashtag = QHashtag.hashtag;
        return from(hashtag)
                .select(hashtag.hashtagName)
                .fetch();
    }
}
