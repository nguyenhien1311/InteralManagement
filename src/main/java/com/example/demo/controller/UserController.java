package com.example.demo.controller;

import com.example.demo.constant.RequestStatus;
import com.example.demo.constant.Role;
import com.example.demo.domain.User;
import com.example.demo.exception.CustomException;
import com.example.demo.request.requests.CreateLateSoonRequest;
import com.example.demo.request.requests.CreateLeaveRequest;
import com.example.demo.request.user.CreateUserRequest;
import com.example.demo.request.user.UpdateUserRequest;
import com.example.demo.response.ResponseObject;
import com.example.demo.response.requests.ListRequestResponse;
import com.example.demo.response.requests.RequestByEmailResponse;
import com.example.demo.response.requests.RequestResponse;
import com.example.demo.response.requests.UpdateStatusRequest;
import com.example.demo.response.user.UserResponse;
import com.example.demo.service.JwtService;
import com.example.demo.service.RequestService;
import com.example.demo.service.UserService;
import com.example.demo.util.JwtData;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "demo/v1/users/")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtService jwtService;
    private final RequestService requestService;

    @PostMapping("admin")
    public ResponseEntity<ResponseObject> createAdmin(@Value("${secret-key}") String SECRET_KEY,
                                                      @Valid @ModelAttribute CreateUserRequest createUserRequest,
                                                      BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new ResponseObject(
                    HttpStatus.BAD_REQUEST.value(),
                    result.getAllErrors()
                            .stream()
                            .map(objectError -> objectError.getDefaultMessage())
                            .collect(Collectors.joining(", ")),
                    null));
        }
        String inputCreateAdminKey = createUserRequest.getKey();
        if ((inputCreateAdminKey == null) || (!inputCreateAdminKey.equals(SECRET_KEY))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObject(
                    HttpStatus.UNAUTHORIZED.value(),
                    "UNAUTHORIZED FOR CREATE ADMIN",
                    null));
        }
        User insertedAdmin = userService.addNewAdmin(createUserRequest);
        String token = jwtService.generateToken(insertedAdmin.getEmail(), insertedAdmin.getRole(), insertedAdmin.getId());

        UserResponse response = new UserResponse(
                insertedAdmin.getId(),
                insertedAdmin.getAvatarId(),
                insertedAdmin.getName(),
                insertedAdmin.getEmail(),
                insertedAdmin.getRole(),
                insertedAdmin.getAccountStatus(),
                token);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK.value(), "CREATE ADMIN SUCCESS", response));
    }

    @PostMapping(value = "user")
    public ResponseEntity<ResponseObject> createUser(@RequestHeader("Authorization") String token,
                                                     @Valid @ModelAttribute CreateUserRequest createUserRequest,
                                                     BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new ResponseObject(
                    HttpStatus.BAD_REQUEST.value(),
                    result.getAllErrors()
                            .stream()
                            .map(objectError -> objectError.getDefaultMessage())
                            .collect(Collectors.joining(", ")),
                    null));
        }
        Role role = Role.valueOf(jwtService.parseTokenToRole(token));
        if (!role.equals(Role.ADMIN)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObject(
                    HttpStatus.UNAUTHORIZED.value(),
                    "UNAUTHORIZED TO CREATE USER",
                    null));
        }
        User insertedUser = userService.addNewUser(createUserRequest);

        String tokenUser = jwtService.generateToken(insertedUser.getEmail(), insertedUser.getRole(), insertedUser.getId());
        UserResponse response = new UserResponse(
                insertedUser.getId(),
                insertedUser.getAvatarId(),
                insertedUser.getName(),
                insertedUser.getEmail(),
                insertedUser.getRole(),
                insertedUser.getAccountStatus(),
                tokenUser);
        return ResponseEntity.ok(new ResponseObject(
                HttpStatus.CREATED.value(),
                "Create User success!",
                response));
    }

    @GetMapping("check/{id}")
    public ResponseEntity<ResponseObject> checkUserById(@PathVariable(name = "id") String id) {
        Optional<User> optionalUser = userService.findById(id);
        if (optionalUser.isEmpty()) {
            throw new CustomException(ResponseObject.STATUS_CODE_NOT_FOUND, ResponseObject.MESSAGE_USER_NOT_FOUND + id);
        }
        User user = optionalUser.get();
        return ResponseEntity.ok(new ResponseObject(HttpStatus.OK.value(), "Get User By Id Success", user));
    }

    @GetMapping("{id}")
    public ResponseEntity<ResponseObject> getUserById(@PathVariable(name = "id") String id) {
        Optional<User> optionalUser = userService.findById(id);
        if (optionalUser.isEmpty()) {
            throw new CustomException(ResponseObject.STATUS_CODE_NOT_FOUND, ResponseObject.MESSAGE_USER_NOT_FOUND + id);
        }
        User user = optionalUser.get();
        UserResponse userResponse = new UserResponse(user.getId(), user.getAvatarId(), user.getName(), user.getEmail(), user.getRole(), user.getAccountStatus(), null);
        return ResponseEntity.ok(new ResponseObject(HttpStatus.OK.value(), "Get User By Id Success", userResponse));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ResponseObject> deleteUserById(@PathVariable(name = "id") String id,
                                                         @RequestHeader("Authorization") String token) {
        Role role = Role.valueOf(jwtService.parseTokenToRole(token));
        if (!role.equals(Role.ADMIN)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObject(
                    HttpStatus.UNAUTHORIZED.value(),
                    "UNAUTHORIZED FOR DELETE USER",
                    null));
        }
        userService.deleteUserById(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK.value(), "DELETED USER WITH ID: " + id + " SUCCESSFULL", null));
    }


    @PostMapping("{id}/latesoon")
    public ResponseEntity<ResponseObject> createLateSoonRequest(@Valid @RequestBody CreateLateSoonRequest lateSoonRequest,
                                                                @RequestHeader("Authorization") String token,
                                                                @PathVariable("id") String id) {
        RequestResponse response = requestService.insertLateSoonRequest(id, lateSoonRequest);
        return ResponseEntity.ok(new ResponseObject(HttpStatus.OK.value(), "Create request complete", response));
    }

    @PutMapping("{id}")
    public ResponseEntity<ResponseObject> updateUserInfo(@PathVariable(name = "id") String id,
                                                         @RequestHeader("Authorization") String token,
                                                         @RequestBody UpdateUserRequest updateUserRequest) {
        JwtData jwtData = jwtService.parseToken(token);
        String userUpdateId = jwtData.getId();
        Role userUpdateRole = jwtData.getRole();
        if ((userUpdateRole.equals(Role.ADMIN.toString())) || (userUpdateId.equals(id))) {
            User user = userService.updateUserInfo(id, updateUserRequest);
            UserResponse userResponse = new UserResponse(user.getId(), user.getAvatarId(), user.getName(), user.getEmail(), user.getRole(), user.getAccountStatus(), null);
            return ResponseEntity.ok(new ResponseObject(HttpStatus.OK.value(), "Update User Info By Id Success", userResponse));

        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObject(HttpStatus.UNAUTHORIZED.value(), ResponseObject.MESSAGE_UNAUTHORIZED, null));
    }

    @PutMapping("{id}/status")
    public ResponseEntity<ResponseObject> changeStatusAccount(@PathVariable(name = "id") String id,
                                                              @RequestHeader("Authorization") String token,
                                                              @RequestParam(name = "setStatus", defaultValue = "ACTIVE", required = false) String setStatus) {
        User user = userService.changeAccountStatus(id, token, setStatus);
        UserResponse userResponse = new UserResponse(user.getId(), user.getAvatarId(), user.getName(), user.getEmail(), user.getRole(), user.getAccountStatus(), null);
        return ResponseEntity.ok(new ResponseObject(HttpStatus.OK.value(), "Change User Account Status Success", userResponse));
    }


    @PostMapping("{id}/off")
    public ResponseEntity<ResponseObject> createLeaveRequest(@Valid @RequestBody CreateLeaveRequest leaveRequest,
                                                             @RequestHeader("Authorization") String token,
                                                             @PathVariable("id") String id) {
        RequestResponse response = requestService.insertLeaveRequest(id, leaveRequest);
        return ResponseEntity.ok(new ResponseObject(HttpStatus.OK.value(), "Create request complete", response));
    }


    @GetMapping({"{id}/requests"})
    public ResponseEntity<ResponseObject> findAll(@PathVariable("id") String id,
                                                  @RequestHeader("Authorization") String token) {
        List<RequestByEmailResponse> list = requestService.findListRequestByUserId(id);
        return ResponseEntity.ok(new ResponseObject(HttpStatus.OK.value(), "Get list request", list));
    }

    @GetMapping({"{id}/requests-to-approve"})
    public ResponseEntity<ResponseObject> findAllRequestsNeedToApprove(@RequestHeader("Authorization") String token,
                                                                       @PathVariable("id") String id) {
        List<RequestByEmailResponse> list = requestService.findListRequestByReceiverEmail(id);
        return ResponseEntity.ok(new ResponseObject(HttpStatus.OK.value(), "Get list request", list));
    }

    @PutMapping({"request/{requestId}"})
    public ResponseEntity<ResponseObject> approveRequest(@PathVariable(name = "requestId") String requestId,
                                                         @RequestBody UpdateStatusRequest status,
                                                         @RequestHeader("Authorization") String token) {
        ListRequestResponse response = requestService.approveRequest(requestId, status, token);
        return ResponseEntity.ok(new ResponseObject(HttpStatus.OK.value(), "Request status set to " + status, response));
    }

}
