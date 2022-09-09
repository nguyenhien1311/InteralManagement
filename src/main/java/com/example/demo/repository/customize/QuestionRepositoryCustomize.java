package com.example.demo.repository.customize;

import com.example.demo.domain.Question;

import java.util.List;

public interface QuestionRepositoryCustomize {
    void deleteAllByCategoryId(String categoryId);

}
