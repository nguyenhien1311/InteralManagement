package com.example.demo.repository.impl;

import com.example.demo.domain.SubComment;
import com.example.demo.repository.customize.SubCommentRepositoryCustomize;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

@RequiredArgsConstructor
public class SubCommentRepositoryImpl implements SubCommentRepositoryCustomize {

    private final MongoTemplate mongoTemplate;

    @Override
    public void deleteAllByCommentId(String commentId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("commentId").is(commentId));
        mongoTemplate.remove(query, SubComment.class);
    }
}
