package com.example.demo.request.category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class CreateCategoryRequest {
    private String banner;
    @NotBlank(message = "Title must not be blank")
    @Size(max = 200,message= "Title not longer than 2000 characters")
    @Indexed(unique = true)
    private String title;
}
