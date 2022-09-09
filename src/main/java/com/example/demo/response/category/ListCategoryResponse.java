package com.example.demo.response.category;

import com.example.demo.domain.Category;
import lombok.*;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListCategoryResponse {
    private List<CategoryResponse> categoryList;
}
