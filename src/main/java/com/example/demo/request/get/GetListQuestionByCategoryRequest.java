package com.example.demo.request.get;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetListQuestionByCategoryRequest {
    @NotBlank(message = "boxId must not be blank")
    String categoryId;
}
