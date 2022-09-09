package com.example.demo.controller;

import com.example.demo.domain.Question;
import com.example.demo.request.get.GetListQuestionByCategoryRequest;
import com.example.demo.request.get.GetListUserQuestionRequest;
import com.example.demo.request.question.UpdateAnswerRequest;
import com.example.demo.response.question.CreateQuestionRequest;
import com.example.demo.response.question.ListQuestionResponse;
import com.example.demo.response.question.QuestionDetailResponse;
import com.example.demo.response.ResponseObject;
import com.example.demo.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("demo/v1/question/")
public class QuestionController {
    private final QuestionService questionService;

    @PostMapping()
    public ResponseEntity<ResponseObject> createQuestion(@RequestHeader("Authorization") String token, @Valid @RequestBody CreateQuestionRequest createQuestionRequest) {
        Question question = questionService.addNewQuestion(token, createQuestionRequest);
        return ResponseEntity.ok(new ResponseObject(HttpStatus.OK.value(), "Create question success", question));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ResponseObject> deleteQuestionById(@PathVariable(name = "id") String id ,@RequestHeader("Authorization") String token) {
        questionService.deleteQuestionById(id, token);
        return ResponseEntity.ok(new ResponseObject(HttpStatus.OK.value(), "Delete question success", null));
    }

    @PutMapping("answer/{id}")
    public ResponseEntity<ResponseObject> answerQuestion(@PathVariable(name = "id") String id , @RequestHeader("Authorization") String token, @Valid @RequestBody UpdateAnswerRequest updateAnswerRequest) {
        Question question = questionService.answerQuestion(id, token, updateAnswerRequest);
        return ResponseEntity.ok(new ResponseObject(HttpStatus.OK.value(), "Answer question success", question));
    }

    @GetMapping("userId/{userId}")
    public ResponseEntity<ResponseObject> listUserQuestionsByPage(@RequestParam(defaultValue = "0", required = false) int skip,
                                                                  @RequestParam(defaultValue = "24", required = false) int limit,
                                                                  @RequestHeader ("Authorization") String token,
                                                                  @PathVariable(name = "userId") String userId
                                                                  ) {
        ListQuestionResponse page = questionService.ListQuestionsByUser(token,userId, skip, limit);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK.value(), "Get list questions by this user success!", page));
    }

    @GetMapping("categoryId/{categoryId}")
    public ResponseEntity<ResponseObject> listCategoryQuestionsByPage(@RequestParam(defaultValue = "0", required = false) int skip,
                                                                      @RequestParam(defaultValue = "24", required = false) int limit,
                                                                      @RequestHeader ("Authorization") String token,
                                                                      @PathVariable(name = "categoryId") String categoryId
                                                                      ) {
        ListQuestionResponse page = questionService.ListQuestionByCategory(token,categoryId,skip,limit);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK.value(), "Get list questions by this categorry success!", page));
    }

    @GetMapping("{id}")
    public ResponseEntity<ResponseObject> getQuestionById(@PathVariable(name = "id") String id, @RequestHeader("Authorization") String token){
        QuestionDetailResponse response = questionService.GetQuestionDetailById(id,token);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK.value(), "Get question detail by id success! ID: " +id, response));
    }
}
