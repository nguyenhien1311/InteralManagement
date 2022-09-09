package com.example.demo.util;

import com.example.demo.constant.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtData {
        private String email;
        private String id;
        private Role role;
}
