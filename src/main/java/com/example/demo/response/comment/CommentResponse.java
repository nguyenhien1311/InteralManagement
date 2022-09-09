package com.example.demo.response.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentResponse {
    private String id;
    private String avatar;
    private String userName;
    private String createTime;
    private String content;
    private int subCommentNumber;
    private String timeReply;
}
