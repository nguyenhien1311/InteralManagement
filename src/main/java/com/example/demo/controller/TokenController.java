package com.example.demo.controller;

import com.example.demo.constant.AccountStatus;
import com.example.demo.domain.User;
import com.example.demo.request.user.LoginRequest;
import com.example.demo.response.ResponseObject;
import com.example.demo.response.user.UserResponse;
import com.example.demo.service.JwtService;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("demo/v1/tokens/")
public class TokenController {
    private final JwtService jwtService;
    private final UserService userService;

    @PostMapping()
    public ResponseEntity<ResponseObject> login(@Valid @RequestBody LoginRequest loginRequest) {
        Optional<User> userCheck = userService.findByEmail(loginRequest.getEmail());
        if (userCheck.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new ResponseObject(
                    HttpStatus.NOT_ACCEPTABLE.value(),
                    "Wrong email!",
                    null));
        }
        User user = userCheck.get();
        if (!user.getPassword().equalsIgnoreCase(loginRequest.getPassword())) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new ResponseObject(
                    HttpStatus.NOT_ACCEPTABLE.value(),
                    "Wrong password!",
                    null));
        }
        if (user.getAccountStatus().toString().equals(AccountStatus.INACTIVE.toString())) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new ResponseObject(
                    HttpStatus.NOT_ACCEPTABLE.value(),
                    "User Inactive",
                    null));
        }
        String token = jwtService.generateToken(user.getEmail(), user.getRole(), user.getId());
        UserResponse response = new UserResponse(user.getId(), user.getAvatarId(), user.getName(), user.getEmail(), user.getRole(), user.getAccountStatus(), token);
        return ResponseEntity.ok(new ResponseObject(
                HttpStatus.OK.value(),
                "Login success",
                response
        ));
    }

}
