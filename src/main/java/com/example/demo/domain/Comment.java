package com.example.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    private String id;
    private String userId;
    private String containerId;
    private String content;
    private Long createCommentTime;
    List<SubComment> subCommentList = new ArrayList<>();
}
