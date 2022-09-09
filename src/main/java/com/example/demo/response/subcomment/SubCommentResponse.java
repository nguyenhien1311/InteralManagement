package com.example.demo.response.subcomment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubCommentResponse {
    private String id;
    private String avatar;
    private String userName;
    private String createTime;
    private String content;
    private String timeReply;
}
