package com.example.demo.repository;

import com.example.demo.domain.Category;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface CategoryRepository extends MongoRepository<Category, String> {
}
