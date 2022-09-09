package com.example.demo.request.news;

import com.example.demo.domain.Comment;
import com.example.demo.domain.HashTag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateNewsRequest {

    private String banner;

    @Size(max = 200, message = "Title length max is 200 character")
    private String title;

    private String content;

    private String hashTag ;
}
