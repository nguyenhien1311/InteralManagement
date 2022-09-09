package com.example.demo.request.news;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateNewsRequest {
    @NotBlank(message = "Banner must not be blank")
    private String banner;

    @NotBlank(message = "Title must not be blank")
    @Size(max = 200, message = "Title length max is 200 character")
    private String title;
    
    @NotBlank(message = "Content must not be blank")
    private String content;

    private String hashTag;

}
