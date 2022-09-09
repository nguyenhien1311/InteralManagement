package com.example.demo.domain;

import com.example.demo.constant.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Question {
    @Id
    private String id;
    private QuestionType questionType;
    private String categoryId;
    private String askById;
    private String answerById;
    private String questionContent;
    private String answer;
    private Long createTime;
    private Long lastUpdateTime;
}
