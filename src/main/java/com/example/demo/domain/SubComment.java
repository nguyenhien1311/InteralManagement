package com.example.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubComment {
    @Id
    private String id;
    private String userId;
    private String containerId;
    private String commentId;
    private String content;
    private Long createCommentTime;
}
