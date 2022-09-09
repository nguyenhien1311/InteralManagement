package com.example.demo.converter;


import com.example.demo.domain.Category;
import com.example.demo.request.category.CreateCategoryRequest;

public class CategoryConverter {
    public static Category convertToInsert(CreateCategoryRequest createCategoryRequest, String userId){
        Category category = new Category();
        category.setBanner(createCategoryRequest.getBanner());
        category.setTitle(createCategoryRequest.getTitle());
        category.setCreatedById(userId);
        Long timeCreateCategory = System.currentTimeMillis();
        category.setCreatedTime(timeCreateCategory);
        category.setLastUpdateTime(timeCreateCategory);
        return category;
    }
}
