package com.example.demo.repository;

import com.example.demo.domain.Image;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ImageRepository extends MongoRepository<Image,String> {
}
