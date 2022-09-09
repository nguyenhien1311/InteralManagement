package com.example.demo.repository;

import com.example.demo.domain.SubComment;
import com.example.demo.repository.customize.SubCommentRepositoryCustomize;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SubCommentRepository extends MongoRepository<SubComment,String> , SubCommentRepositoryCustomize {
    void deleteAllByContainerId(String containerId);
}
