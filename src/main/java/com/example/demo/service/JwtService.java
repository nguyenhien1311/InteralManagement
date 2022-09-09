package com.example.demo.service;

import com.example.demo.constant.Role;
import com.example.demo.util.JwtData;
import io.jsonwebtoken.Claims;

import java.util.Map;


public interface JwtService {
    String generateToken(String email, Role role, String id);
    String parseTokenToRole(String token);
    String parseTokenToId(String token);
    Map<String, Object> parseTokenToClaims(String token);

    JwtData parseToken(String token);
}
