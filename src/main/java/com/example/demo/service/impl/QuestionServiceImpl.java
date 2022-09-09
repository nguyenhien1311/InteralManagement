package com.example.demo.service.impl;

import com.example.demo.constant.QuestionType;
import com.example.demo.constant.Role;
import com.example.demo.converter.QuestionConverter;
import com.example.demo.domain.*;
import com.example.demo.exception.CustomException;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.QuestionRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.request.question.UpdateAnswerRequest;
import com.example.demo.response.*;
import com.example.demo.response.question.CreateQuestionRequest;
import com.example.demo.response.question.ListQuestionResponse;
import com.example.demo.response.question.QuestionDetailResponse;
import com.example.demo.response.question.QuestionOverviewResponse;
import com.example.demo.service.JwtService;
import com.example.demo.service.QuestionService;
import com.example.demo.util.JwtData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    private final CategoryRepository categoryRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;


    @Override
    public Question addNewQuestion(String token, CreateQuestionRequest createQuestionRequest) {
        Optional<Category> optionalCategory = categoryRepository.findById(createQuestionRequest.getCategoryId());
        if (!optionalCategory.isPresent()) {
            throw new CustomException(ResponseObject.STATUS_CODE_NOT_FOUND, ResponseObject.MESSAGE_CATEGORY_NOT_FOUND);
        }
        JwtData jwtData = jwtService.parseToken(token);
        String userAskRole = jwtData.getRole().toString();
        String userIdAsk = jwtData.getId();
        if (userAskRole.equals(Role.ADMIN)) {
            // answerById and answer == null
            Question newQuestion = new Question();
            newQuestion.setQuestionType(QuestionType.BYADMIN);
            newQuestion.setCategoryId(createQuestionRequest.getCategoryId());
            newQuestion.setAskById(userIdAsk);
            newQuestion.setQuestionContent(createQuestionRequest.getQuestionContent());
            Long timeCreate = System.currentTimeMillis();
            newQuestion.setCreateTime(timeCreate);
            newQuestion.setLastUpdateTime(timeCreate);
            questionRepository.insert(newQuestion);

            Category category = categoryRepository.findById(createQuestionRequest.getCategoryId()).get();
            category.getQuestionList().add(newQuestion);
            categoryRepository.save(category);

            return newQuestion;
        } else if (userAskRole.equals(Role.USER)) {
            // boxId, answerById and answer == null
            Question newQuestion = new Question();
            newQuestion.setQuestionType(QuestionType.BYUSER);
            newQuestion.setAskById(userIdAsk);
            newQuestion.setQuestionContent(createQuestionRequest.getQuestionContent());
            Long timeCreate = System.currentTimeMillis();
            newQuestion.setCreateTime(timeCreate);
            newQuestion.setLastUpdateTime(timeCreate);
            questionRepository.insert(newQuestion);

            return newQuestion;
        } else {
            throw new CustomException(ResponseObject.STATUS_CODE_UNAUTHORIZED, ResponseObject.MESSAGE_UNAUTHORIZED_TO_CREATE_QUESTION);
        }
    }

    @Override
    public void deleteQuestionById(String id, String token) {
        Question question = questionRepository.findById(id).get();
        if (question == null) {
            throw new CustomException(ResponseObject.STATUS_CODE_NOT_FOUND, ResponseObject.MESSAGE_OBJECT_NOT_FOUND + id);
        }
        Role role = Role.valueOf(jwtService.parseTokenToRole(token));
        String userIdDelete = jwtService.parseTokenToId(token);
        if ((role.equals(Role.ADMIN)) || userIdDelete.equals(question.getAskById())) {
            if(question.getCategoryId() != null){
                Category category = categoryRepository.findById(question.getCategoryId()).get();
                if(category != null){
                    category.getQuestionList().remove(question);
                    categoryRepository.save(category);
                }
            }
            questionRepository.deleteById(id);
        } else {
            throw new CustomException(ResponseObject.STATUS_CODE_UNAUTHORIZED, ResponseObject.MESSAGE_UNAUTHORIZED_TO_DELETE_QUESTION + id);
        }

    }

    @Override
    public Question answerQuestion(String id, String token, UpdateAnswerRequest updateAnswerRequest) {
        Question question = questionRepository.findById(id).get();
        if (question == null) {
            throw new CustomException(ResponseObject.STATUS_CODE_NOT_FOUND, ResponseObject.MESSAGE_OBJECT_NOT_FOUND + id);
        }
        Role role = Role.valueOf(jwtService.parseTokenToRole(token));
        String userIdAnswer = jwtService.parseTokenToId(token);
        if (role.equals(Role.ADMIN)) {
            question.setAnswer(updateAnswerRequest.getAnswer());
            question.setAnswerById(userIdAnswer);
            question.setLastUpdateTime(System.currentTimeMillis());
            questionRepository.save(question);
            return question;
        } else {
            throw new CustomException(ResponseObject.STATUS_CODE_UNAUTHORIZED, ResponseObject.MESSAGE_UNAUTHORIZED_TO_ANSWER_QUESTION + id);
        }
    }

    @Override
    public ListQuestionResponse ListQuestionsByUser(String token, String userIdAsk, int skip, int limit) {
        Optional<User> optionalUserAsk = userRepository.findById(userIdAsk);
        if (optionalUserAsk.isEmpty()) {
            throw new CustomException(ResponseObject.STATUS_CODE_NOT_FOUND, ResponseObject.MESSAGE_USER_NOT_FOUND + userIdAsk);
        }
        User userAsk = optionalUserAsk.get();
        String idViewer = jwtService.parseTokenToId(token);
        Role roleViewer = Role.valueOf(jwtService.parseTokenToRole(token));
        if ((idViewer.equals(userIdAsk)) || (roleViewer.equals(Role.ADMIN))) {
            List<Question> collect = questionRepository.findAllByAskByIdOrderByLastUpdateTimeDesc(userIdAsk);
            List<QuestionOverviewResponse> collect1 = new ArrayList<>();
            collect.forEach(question -> {
                QuestionOverviewResponse questionOverviewResponse = QuestionConverter.fromQuestionToResponse(question);
                collect1.add(questionOverviewResponse);
            });
            return new ListQuestionResponse(collect1);
        } else {
            throw new CustomException(ResponseObject.STATUS_CODE_UNAUTHORIZED, ResponseObject.MESSAGE_UNAUTHORIZED_TO_VIEW_LIST_QUESTION + userIdAsk);
        }
    }

    @Override
    public ListQuestionResponse ListQuestionByCategory(String token, String categoryId, int skip, int limit) {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        if (optionalCategory.isEmpty()) {
            throw new CustomException(ResponseObject.STATUS_CODE_NOT_FOUND, ResponseObject.MESSAGE_CATEGORY_NOT_FOUND + categoryId);
        }
        String userGetRole = jwtService.parseTokenToRole(token);
        if(userGetRole.equals(Role.ADMIN.toString()) || userGetRole.equals(Role.USER.toString())){
            List<Question> collect = questionRepository.findAllByCategoryIdOrderByLastUpdateTimeDesc(categoryId);
            List<QuestionOverviewResponse> collect1 = new ArrayList<>();
            collect.forEach(question -> {
                QuestionOverviewResponse questionOverviewResponse = QuestionConverter.fromQuestionToResponse(question);
                collect1.add(questionOverviewResponse);
            });
            return new ListQuestionResponse(collect1);
        }else {
            throw new CustomException(ResponseObject.STATUS_CODE_UNAUTHORIZED, ResponseObject.MESSAGE_UNAUTHORIZED_TO_VIEW_LIST_QUESTION );
        }
    }

    @Override
    public QuestionDetailResponse GetQuestionDetailById(String questionId, String token) {
        Question question = questionRepository.findById(questionId).get();
        if (question == null) {
            throw new CustomException(ResponseObject.STATUS_CODE_NOT_FOUND, ResponseObject.MESSAGE_QUESTION_NOT_FOUND + questionId);
        }
        String userGetRole = jwtService.parseTokenToRole(token);
        String userAskId = question.getAskById();
        User userAsk = userRepository.findById(userAskId).get();
        String userAskRole = userAsk.getRole().toString();
        if(userGetRole.equals(userAskRole) || userGetRole.equals(Role.ADMIN.toString())){
            QuestionDetailResponse questionDetailResponse = new QuestionDetailResponse();
            questionDetailResponse.setQuestionContent(question.getQuestionContent());
            questionDetailResponse.setAnswer(question.getAnswer());
            return questionDetailResponse;
        }
        throw new CustomException(ResponseObject.STATUS_CODE_UNAUTHORIZED, ResponseObject.MESSAGE_UNAUTHORIZED + questionId);
    }
}
