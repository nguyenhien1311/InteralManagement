package com.example.demo.response.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDetailResponse {
    private String categoryId;
    private String banner;
    private String title;
    private String createdById;
    private String updateById;
    private Long createdTime;
    private Long lastUpdateTime;
    private Integer questionsNum = new Integer(0);
}
