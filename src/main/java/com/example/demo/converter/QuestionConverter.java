package com.example.demo.converter;

import com.example.demo.domain.Question;
import com.example.demo.response.question.QuestionOverviewResponse;

public class QuestionConverter {
    public static QuestionOverviewResponse fromQuestionToResponse(Question question) {
        return QuestionOverviewResponse.builder()
                .index(1)
                .question(question.getQuestionContent())
                .questionId(question.getId())
                .build();
    }
}
