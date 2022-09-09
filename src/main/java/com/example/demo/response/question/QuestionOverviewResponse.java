package com.example.demo.response.question;

import com.example.demo.constant.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionOverviewResponse {
    private String question;
    private Integer index;
    private String questionId;
}
