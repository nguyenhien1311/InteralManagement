package com.example.demo.repository.impl;

import com.example.demo.domain.Question;
import com.example.demo.repository.customize.QuestionRepositoryCustomize;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

@RequiredArgsConstructor
public class QuestionRepositoryCustomizeImpl implements QuestionRepositoryCustomize {

    private final MongoTemplate mongoTemplate;

    @Override
    public void deleteAllByCategoryId(String categoryId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("categoryId").is(categoryId));
        mongoTemplate.remove(query, Question.class);
    }
}
