package com.example.demo.service.impl;

import com.example.demo.constant.Role;
import com.example.demo.converter.CategoryConverter;
import com.example.demo.domain.Category;
import com.example.demo.domain.Question;
import com.example.demo.exception.CustomException;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.QuestionRepository;
import com.example.demo.request.category.UpdateCategoryRequest;
import com.example.demo.request.category.CreateCategoryRequest;
import com.example.demo.response.ResponseObject;
import com.example.demo.response.category.CategoryDetailResponse;
import com.example.demo.response.category.CategoryResponse;
import com.example.demo.response.category.ListCategoryResponse;
import com.example.demo.service.CategoryService;
import com.example.demo.service.JwtService;
import com.example.demo.util.JwtData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    private final QuestionRepository questionRepository;
    private final JwtService jwtService;

    @Override
    public List<Category> findAll() {
        List<Category> list = categoryRepository.findAll();
        return list;
    }

    @Override
    public Category insert(@Valid CreateCategoryRequest createCategoryRequest, String token) {
        Role role = Role.valueOf(jwtService.parseTokenToRole(token));
        if (!role.equals(Role.ADMIN)) {
            throw new CustomException(ResponseObject.STATUS_CODE_UNAUTHORIZED, ResponseObject.MESSAGE_UNAUTHORIZED);
        }
        String userId = jwtService.parseTokenToId(token);
        Category category = CategoryConverter.convertToInsert(createCategoryRequest, userId);
        Category insertedCategory = categoryRepository.insert(category);
        return insertedCategory;
    }

    @Override
    public Category save(String id, String token, UpdateCategoryRequest updateCategoryRequest) {
        Category categoryToUpdate = categoryRepository.findById(id).get();
        if (categoryToUpdate == null) {
            throw new CustomException(ResponseObject.STATUS_CODE_NOT_FOUND, ResponseObject.MESSAGE_CATEGORY_NOT_FOUND + id);
        }
        Role role = Role.valueOf(jwtService.parseTokenToRole(token));
        if (!role.equals(Role.ADMIN)) {
            throw new CustomException(ResponseObject.STATUS_CODE_UNAUTHORIZED, ResponseObject.MESSAGE_UNAUTHORIZED);
        }
        if (!updateCategoryRequest.getBanner().isBlank()) {
            categoryToUpdate.setBanner(updateCategoryRequest.getBanner());
        }
        if (!updateCategoryRequest.getTitle().isBlank()) {
            categoryToUpdate.setTitle(updateCategoryRequest.getTitle());
        }
        String userIdUpdate = jwtService.parseTokenToId(token);
        categoryToUpdate.setUpdateById(userIdUpdate);
        categoryToUpdate.setLastUpdateTime(System.currentTimeMillis());
        categoryRepository.save(categoryToUpdate);
        return categoryToUpdate;
    }

    @Override
    public void delete(String id, String token) {
        Category categoryToDelete = categoryRepository.findById(id).get();
        if (categoryToDelete == null) {
            throw new CustomException(ResponseObject.STATUS_CODE_NOT_FOUND, ResponseObject.MESSAGE_CATEGORY_NOT_FOUND + id);
        }
        String userDeleteId = jwtService.parseTokenToId(token);
        if (!userDeleteId.equals(categoryToDelete.getCreatedById())) {
            throw new CustomException(ResponseObject.STATUS_CODE_UNAUTHORIZED, ResponseObject.MESSAGE_UNAUTHORIZED);
        }
        questionRepository.deleteAllByCategoryId(id);
        categoryRepository.deleteById(id);
    }

    @Override
    public CategoryDetailResponse getCategoryDetailById(String token, String id) {
        Category category = categoryRepository.findById(id).get();
        if (category == null) {
            throw new CustomException(ResponseObject.STATUS_CODE_NOT_FOUND, ResponseObject.MESSAGE_CATEGORY_NOT_FOUND + id);
        }
        JwtData jwtData = jwtService.parseToken(token);
        Role role = jwtData.getRole();
        if (role.equals(Role.USER) || role.equals(Role.ADMIN)) {
            CategoryDetailResponse response = new CategoryDetailResponse(
                    category.getId(),
                    category.getBanner(),
                    category.getTitle(),
                    category.getCreatedById(),
                    category.getCreatedById(),
                    category.getCreatedTime(),
                    category.getLastUpdateTime(),
                    category.getQuestionList().size()
            );
            return response;
        } else {
            throw new CustomException(ResponseObject.STATUS_CODE_UNAUTHORIZED, ResponseObject.MESSAGE_UNAUTHORIZED);
        }
    }

    @Override
    public ListCategoryResponse listCategoryByPage(String token, int skip, int limit) {
        JwtData jwtData = jwtService.parseToken(token);
        Role role = jwtData.getRole();
        if (role.equals(Role.USER) || role.equals(Role.ADMIN)) {
            List<CategoryResponse> collect = categoryRepository.findAll()
                    .stream()
                    .sorted(Comparator.comparing(Category::getLastUpdateTime).reversed())
                    .map(n -> {
                        List<Question> questionList = n.getQuestionList();
                        int countQuestions = questionList.size();
                        return CategoryResponse.builder()
                                .banner(n.getBanner())
                                .title(n.getTitle())
                                .questionsNum(countQuestions)
                                .build();
                    })
                    .skip((long) skip * limit)
                    .limit(limit)
                    .collect(Collectors.toList());
            return new ListCategoryResponse(collect);
        } else {
            throw new CustomException(ResponseObject.STATUS_CODE_UNAUTHORIZED, ResponseObject.MESSAGE_UNAUTHORIZED);
        }
    }
}
