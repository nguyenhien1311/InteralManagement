package com.example.demo.repository.impl;

import com.example.demo.domain.News;
import com.example.demo.repository.customize.NewsRepositoryCustomize;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class NewsRepositoryCustomizeImpl implements NewsRepositoryCustomize {
    private final MongoTemplate mongoTemplate;

    @Override
    public List<News> findAllByHashTag(String hashTag, int skip, int limit) {
        Query query = new Query();
        if (hashTag != null) {
            query.addCriteria(Criteria.where("hashTag").is(hashTag));
        }
        query.skip((long) skip * limit);
        query.limit(limit);
        query.with(Sort.by(Sort.Direction.DESC, "createTime"));
        return mongoTemplate.find(query, News.class,"news");
    }
}
