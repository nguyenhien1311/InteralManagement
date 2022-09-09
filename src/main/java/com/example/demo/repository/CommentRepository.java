package com.example.demo.repository;

import com.example.demo.domain.Comment;
import com.example.demo.repository.customize.CommentRepositoryCustomize;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommentRepository extends MongoRepository<Comment,String>, CommentRepositoryCustomize {
    Comment findCommentById(String id);

    void deleteAllByContainerId(String containerId);

}
