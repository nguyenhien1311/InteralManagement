package com.example.demo.repository;

import com.example.demo.domain.News;
import com.example.demo.repository.customize.NewsRepositoryCustomize;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NewsRepository extends MongoRepository<News, String>{

}
