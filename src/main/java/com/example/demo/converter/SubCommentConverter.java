package com.example.demo.converter;

import com.example.demo.domain.SubComment;
import com.example.demo.request.subcomment.CreateSubCommentRequest;

public class SubCommentConverter {
    public static SubComment convertToInsert(String commentId, String newsId, String userId, CreateSubCommentRequest createSubCommentRequest){
        SubComment subComment = new SubComment();
        subComment.setCommentId(commentId);
        subComment.setContainerId(newsId);
        subComment.setUserId(userId);
        subComment.setContent(createSubCommentRequest.getContent());
        subComment.setCreateCommentTime(System.currentTimeMillis());
        return subComment;
    }
}
