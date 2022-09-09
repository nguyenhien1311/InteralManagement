package com.example.demo.response.question;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListQuestionResponse {
    private List<QuestionOverviewResponse> questionOverviewResponseList;
}
