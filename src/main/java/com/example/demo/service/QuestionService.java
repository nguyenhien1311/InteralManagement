package com.example.demo.service;

import com.example.demo.domain.Question;
import com.example.demo.request.question.UpdateAnswerRequest;
import com.example.demo.response.question.CreateQuestionRequest;
import com.example.demo.response.question.ListQuestionResponse;
import com.example.demo.response.question.QuestionDetailResponse;

public interface QuestionService {
    Question addNewQuestion(String token, CreateQuestionRequest createQuestionRequest);

    void deleteQuestionById(String id,String token);

    Question answerQuestion(String id, String token, UpdateAnswerRequest updateAnswerRequest);

    public ListQuestionResponse ListQuestionsByUser(String token,String userIdAsk,int skip,int limit);

    ListQuestionResponse ListQuestionByCategory(String token, String boxId, int skip, int limit);

    QuestionDetailResponse GetQuestionDetailById(String questionId,String token);
}
