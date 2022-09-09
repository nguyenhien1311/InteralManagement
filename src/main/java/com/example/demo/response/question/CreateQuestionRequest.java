package com.example.demo.response.question;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateQuestionRequest {
    String categoryId;
    @NotBlank(message = "Question content must not be blank")
    String questionContent;
}
