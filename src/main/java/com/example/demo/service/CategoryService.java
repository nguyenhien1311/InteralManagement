package com.example.demo.service;

import com.example.demo.domain.Category;
import com.example.demo.request.category.UpdateCategoryRequest;
import com.example.demo.request.category.CreateCategoryRequest;
import com.example.demo.response.category.CategoryDetailResponse;
import com.example.demo.response.category.ListCategoryResponse;

import javax.validation.Valid;
import java.util.List;

public interface CategoryService {
    List<Category> findAll();

    Category insert(@Valid CreateCategoryRequest createCategoryRequest, String token);

    Category save(String id, String token, UpdateCategoryRequest updateCategoryRequest);

    void delete(String id, String token);
    CategoryDetailResponse getCategoryDetailById(String token,String id);

    public ListCategoryResponse listCategoryByPage(String token,int skip, int limit);
}
