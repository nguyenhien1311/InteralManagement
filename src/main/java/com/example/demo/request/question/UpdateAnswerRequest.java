package com.example.demo.request.question;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAnswerRequest {
    @NotBlank(message = "Answer must not be blank")
    private String answer;
}
