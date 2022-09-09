package com.example.demo.service;

import com.example.demo.request.comment.CreateCommentRequest;
import com.example.demo.request.news.CreateNewsRequest;
import com.example.demo.request.subcomment.CreateSubCommentRequest;
import com.example.demo.request.news.UpdateNewsRequest;
import com.example.demo.response.comment.CommentResponse;
import com.example.demo.response.news.NewsResponse;
import com.example.demo.response.news.NewsSearchResponse;
import com.example.demo.response.subcomment.SubCommentResponse;

import java.util.List;

public interface NewsService {

     NewsResponse getNewsById(String id);

     NewsResponse save(String id, String token, UpdateNewsRequest updateNewsRequest);

     NewsResponse insert(String token, CreateNewsRequest CreateNewsRequest);

     void deleteNewsById(String id, String token);

     NewsSearchResponse listByPage(int pageNum, int pageSize, String hashTag);

     String addCommentToNews(String newsId, String token, CreateCommentRequest createCommentRequest);

     void deleteCommentById(String subCommentId, String token);

     String addSubCommentToNews(String commentId, String token, CreateSubCommentRequest createSubCommentRequest);

     void deleteSubCommentById(String subCommentId, String token);

    List<CommentResponse> getListComment(String newsId, String token, int skip, int take);

    List<SubCommentResponse> getListSubComment(String commentId,String token, int skip, int take);
}
