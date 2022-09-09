package com.example.demo.repository.impl;

import com.example.demo.domain.Comment;
import com.example.demo.repository.customize.CommentRepositoryCustomize;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustomize {
    private final MongoTemplate mongoTemplate;

    @Override
    public void deleteAllByContainerId(String containerId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("containerId").is(containerId));
        mongoTemplate.remove(query, Comment.class);
    }
}
