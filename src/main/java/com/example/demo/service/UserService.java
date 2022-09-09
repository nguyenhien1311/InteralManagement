package com.example.demo.service;

import com.example.demo.domain.User;
import com.example.demo.request.user.CreateUserRequest;
import com.example.demo.request.user.UpdateUserRequest;

import java.util.Optional;


public interface UserService {
    Optional<User> findByEmail(String email);

    Optional<User> findById(String id);

    public User addNewAdmin(CreateUserRequest createUserRequest);

    public User addNewUser(CreateUserRequest createUserRequest);

    public User changeAccountStatus(String id, String token, String setAccountStatus);

    public User updateUserInfo(String id, UpdateUserRequest updateUserRequest);

    public void deleteUserById(String id);

}
