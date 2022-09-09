package com.example.demo.response.user;

import com.example.demo.constant.AccountStatus;
import com.example.demo.constant.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private String id;
    private String avatar;
    private String name;
    private String email;
    private Role role;
    private AccountStatus accountStatus;
    private String token;
}
