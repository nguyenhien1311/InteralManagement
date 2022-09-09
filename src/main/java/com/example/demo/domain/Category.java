package com.example.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Category {
    @Id
    private String id;
    private String banner;
    @Indexed(unique = true)
    private String title;
    private List<Question> questionList = new ArrayList<>();
    private String createdById;
    private String updateById;
    private Long createdTime;
    private Long lastUpdateTime;
}
