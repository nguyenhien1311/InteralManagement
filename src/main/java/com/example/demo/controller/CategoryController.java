package com.example.demo.controller;

import com.example.demo.domain.Category;
import com.example.demo.request.category.UpdateCategoryRequest;
import com.example.demo.response.ResponseObject;
import com.example.demo.request.category.CreateCategoryRequest;
import com.example.demo.response.category.CategoryDetailResponse;
import com.example.demo.response.category.CategoryResponse;
import com.example.demo.response.category.ListCategoryResponse;
import com.example.demo.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("demo/v1/categories/")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping()
    public ResponseEntity<?> createCategory(@Valid @RequestBody CreateCategoryRequest createCategoryRequest, @RequestHeader("Authorization") String token) {
        Category category = categoryService.insert(createCategoryRequest, token);
        return ResponseEntity.ok(
                new ResponseObject(
                        HttpStatus.OK.value(),
                        "Insert category complete",
                        category)
        );
    }
    @PutMapping("{id}")
    public ResponseEntity<?> updateCategory(@PathVariable(name = "id")String id,
                                       @RequestHeader("Authorization") String token,
                                       @Valid @RequestBody UpdateCategoryRequest updateCategoryRequest) {
        Category category = categoryService.save(id, token, updateCategoryRequest);
        return ResponseEntity.ok(
                new ResponseObject(
                        HttpStatus.OK.value(),
                        "Update Category Complete",
                        category)
        );
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getCategoryById(@RequestHeader("Authorization") String token,
                                                @PathVariable(name = "id")String id) {
        CategoryDetailResponse response = categoryService.getCategoryDetailById(token,id);
        return ResponseEntity.ok(
                new ResponseObject(
                        HttpStatus.OK.value(),
                        "Get Category Info By Id Success. ID: "+ id ,
                        response)
        );
    }
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteCategoryById(@PathVariable(name = "id") String id, @RequestHeader("Authorization") String token) {
        categoryService.delete(id,token);
        return ResponseEntity.ok(
                new ResponseObject(
                        HttpStatus.OK.value(),
                        "Delete Category Success",
                        null)
        );
    }

    @GetMapping("list")
    public ResponseEntity<ResponseObject> listByPage(@RequestHeader("Authorization") String token,
                                                    @RequestParam(defaultValue = "0", required = false) int skip,
                                                     @RequestParam(defaultValue = "24", required = false) int limit
                                                     ) {
        ListCategoryResponse page = categoryService.listCategoryByPage(token,skip, limit);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK.value(), "Get list Category pagination success!", page));
    }

}
