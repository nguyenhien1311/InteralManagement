package com.example.demo.service.impl;

import com.example.demo.constant.AccountStatus;
import com.example.demo.constant.Role;
import com.example.demo.domain.Image;
import com.example.demo.domain.User;
import com.example.demo.exception.CustomException;
import com.example.demo.repository.ImageRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.request.user.CreateUserRequest;
import com.example.demo.request.user.UpdateUserRequest;
import com.example.demo.response.ResponseObject;
import com.example.demo.service.JwtService;
import com.example.demo.service.UserService;
import lombok.AllArgsConstructor;
import org.bson.types.Binary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    private final ImageRepository imageRepository;

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findById(String id) {
        return userRepository.findById(id);
    }

    @Override
    public User addNewAdmin(CreateUserRequest createUserRequest) {
        if (createUserRequest.getFile() == null) {
            throw new CustomException(ResponseObject.STATUS_CODE_NOT_ACCEPTABLE, ResponseObject.MESSAGE_NULL_IMAGE);
        }
        if (createUserRequest.getEmail() == null) {
            throw new CustomException(ResponseObject.STATUS_CODE_NOT_ACCEPTABLE, ResponseObject.MESSAGE_NULL_EMAIL);
        }
        String imageId = insertImage(createUserRequest.getFile());
        User newAdmin = new User();
        newAdmin.setAvatarId(imageId);
        newAdmin.setName(createUserRequest.getName());
        newAdmin.setEmail(createUserRequest.getEmail());
        newAdmin.setPassword(createUserRequest.getPassword());
        newAdmin.setRole(Role.ADMIN);
        newAdmin.setAccountStatus(AccountStatus.ACTIVE);
        userRepository.insert(newAdmin);
        return newAdmin;
    }

    @Override
    public User addNewUser(CreateUserRequest createUserRequest) {
        if (createUserRequest.getFile() == null) {
            throw new CustomException(ResponseObject.STATUS_CODE_NOT_ACCEPTABLE, ResponseObject.MESSAGE_NULL_IMAGE);
        }
        if (createUserRequest.getEmail() == null) {
            throw new CustomException(ResponseObject.STATUS_CODE_NOT_ACCEPTABLE, ResponseObject.MESSAGE_NULL_EMAIL);
        }
        String imageId = insertImage(createUserRequest.getFile());
        User newUser = new User();
        newUser.setAvatarId(imageId);
        newUser.setName(createUserRequest.getName());
        newUser.setEmail(createUserRequest.getEmail());
        newUser.setPassword(createUserRequest.getPassword());
        newUser.setRole(Role.USER);
        newUser.setAccountStatus(AccountStatus.ACTIVE);
        userRepository.insert(newUser);
        return newUser;
    }

    @Override
    public User changeAccountStatus(String id, String token, String setAccountStatus) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new CustomException(ResponseObject.STATUS_CODE_NOT_FOUND, ResponseObject.MESSAGE_USER_NOT_FOUND + id);
        }
        User user = userOptional.get();
        String roleUserFix = jwtService.parseTokenToRole(token);

        if ((roleUserFix.equals(Role.ADMIN.toString())) && (user.getRole().equals(Role.USER))) {
            if (setAccountStatus.equals(AccountStatus.INACTIVE.toString())) {
                user.setAccountStatus(AccountStatus.INACTIVE);
            } else if (setAccountStatus.equals(AccountStatus.ACTIVE.toString())) {
                user.setAccountStatus(AccountStatus.ACTIVE);
            } else {
                throw new CustomException(ResponseObject.STATUS_CODE_BAD_REQUEST, ResponseObject.MESSAGE_PARAM_INPUT_NOT_ACCEPTABLE);
            }
        } else {
            throw new CustomException(ResponseObject.STATUS_CODE_UNAUTHORIZED, ResponseObject.MESSAGE_UNAUTHORIZED);
        }

        userRepository.save(user);
        return user;
    }

    @Override
    public User updateUserInfo(String id, UpdateUserRequest updateUserRequest) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new CustomException(ResponseObject.STATUS_CODE_NOT_FOUND, ResponseObject.MESSAGE_USER_NOT_FOUND + id);
        }
        User userToUpdate = optionalUser.get();
        if (updateUserRequest.getAvatar() != null) {
            userToUpdate.setAvatarId(updateUserRequest.getAvatar());
        }
        if (updateUserRequest.getName() != null) {
            userToUpdate.setName(updateUserRequest.getName());
        }
        if (updateUserRequest.getEmail() != null) {
            userToUpdate.setEmail(updateUserRequest.getEmail());
        }
        if (updateUserRequest.getPassword() != null) {
            userToUpdate.setPassword(updateUserRequest.getPassword());
        }
        userRepository.save(userToUpdate);
        return userToUpdate;
    }

    public void deleteUserById(String id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new CustomException(ResponseObject.STATUS_CODE_NOT_FOUND, ResponseObject.MESSAGE_USER_NOT_FOUND + id);
        }
        userRepository.deleteById(id);
    }


    public String insertImage(MultipartFile file) {
        Image image = new Image();
        try {
            image.setName(file.getOriginalFilename());
            image.setCreatTime(System.currentTimeMillis());
            image.setContent(new Binary(file.getBytes()));
            image.setContentType(file.getContentType());
            image.setSize(file.getSize());
        } catch (IOException e) {
            throw new CustomException(ResponseObject.STATUS_CODE_UNKNOWN_FAILED, ResponseObject.MESSAGE_FAIL_TO_UPLOAD_IMAGE);
        }
        Image savedImage = imageRepository.save(image);
        return savedImage.getId();
    }
}
