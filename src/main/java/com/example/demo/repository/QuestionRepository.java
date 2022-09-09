package com.example.demo.repository;

import com.example.demo.domain.Question;
import com.example.demo.repository.customize.CommentRepositoryCustomize;
import com.example.demo.repository.customize.QuestionRepositoryCustomize;
import com.example.demo.repository.impl.CommentRepositoryImpl;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface QuestionRepository extends MongoRepository<Question,String>, QuestionRepositoryCustomize {
    void deleteAllByCategoryId(String categoryId);

    List<Question> findAllByCategoryIdOrderByLastUpdateTimeDesc(String categoryId);
    List<Question> findAllByAskByIdOrderByLastUpdateTimeDesc(String askById);
}
