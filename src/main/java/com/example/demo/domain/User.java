package com.example.demo.domain;

import com.example.demo.constant.AccountStatus;
import com.example.demo.constant.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class User {
    @Id
    private String id;
    @Field("avatar_id")
    private String avatarId;
    private String name;
    @Indexed(unique = true)
    private String email;
    private String password;
    private Role role;
    @Field("account_status")
    private AccountStatus accountStatus;
}
