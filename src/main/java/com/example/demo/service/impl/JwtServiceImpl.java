package com.example.demo.service.impl;

import com.example.demo.constant.Role;
import com.example.demo.service.JwtService;
import com.example.demo.util.JwtData;
import com.example.demo.util.StringProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
@Service
public class JwtServiceImpl implements JwtService {

    @Override
    public String generateToken(String email, Role role,String id) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(StringProvider.EMAIL, email);
        claims.put(StringProvider.ROLE, role);
        claims.put(StringProvider.ID, id);
        String token = Jwts.builder()
                .addClaims(claims)
                .signWith(SignatureAlgorithm.HS256, StringProvider.KEY.getBytes())
                .compact();
        return token;
    }

    @Override
    public String parseTokenToRole(String token) {
        token = token.replace("Bearer ", "");
        Claims claims = Jwts.parserBuilder().setSigningKey(StringProvider.KEY.getBytes()).build()
                .parseClaimsJws(token).getBody();
        return claims.get(StringProvider.ROLE).toString();
    }

    @Override
    public String parseTokenToId(String token) {
        token = token.replace("Bearer ", "");
        Claims claims = Jwts.parserBuilder().setSigningKey(StringProvider.KEY.getBytes()).build().
                parseClaimsJws(token).getBody();
        return claims.get(StringProvider.ID).toString();
    }
    @Override
    public Map<String, Object>  parseTokenToClaims(String token) {
        token = token.replace("Bearer ", "");
        Map<String, Object> claims = Jwts.parserBuilder().setSigningKey(StringProvider.KEY.getBytes()).build().
                parseClaimsJws(token).getBody();
        return claims;
    }

    @Override
    public JwtData parseToken(String token) {
        token = token.replace("Bearer ", "");
        Claims claims = Jwts.parserBuilder().setSigningKey(StringProvider.KEY.getBytes()).build().
                parseClaimsJws(token).getBody();
        JwtData result = JwtData.builder()
                .id(claims.get(StringProvider.ID).toString())
                .email(claims.get(StringProvider.EMAIL).toString())
                .role(Role.valueOf(claims.get(StringProvider.ROLE).toString()))
                .build();
        return result;
    }

}
