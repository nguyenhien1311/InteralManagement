package com.example.demo.repository;

import com.example.demo.domain.*;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User,String> {
    Optional<User> findByEmail(String email);

    User findUserById(String id);

    boolean existsUserByEmail(String email);
}
