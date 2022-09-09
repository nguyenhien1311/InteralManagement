package com.example.demo.controller;

import com.example.demo.request.subcomment.CreateSubCommentRequest;
import com.example.demo.response.comment.CommentResponse;
import com.example.demo.response.ResponseObject;
import com.example.demo.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("demo/v1/comments/")
@RequiredArgsConstructor
public class CommentController {
    private final NewsService newsService;

    @GetMapping()
    public ResponseEntity<ResponseObject> getListComment(@RequestHeader("Authorization") String token,
                                                         @RequestParam(defaultValue = "0", required = false) int skip,
                                                         @RequestParam(defaultValue = "24", required = false) int limit,
                                                         @RequestParam String newsId) {
        List<CommentResponse> listComment = newsService.getListComment(newsId,token, skip, limit);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK.value(), "GET LIST COMMENT SUCCESS", listComment));
    }

    @PostMapping("{commentId}/subcomment")
    public ResponseEntity<ResponseObject> addSubCommentToNews(@RequestHeader("Authorization") String token,
                                                              @Valid @RequestBody CreateSubCommentRequest createSubCommentRequest,
                                                              @PathVariable("commentId") String commentId) {
        String addSubCommentId = newsService.addSubCommentToNews(commentId, token, createSubCommentRequest);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK.value(), "ADD SUB COMMENT TO NEWS SUCCESS","SubComment ID :" + addSubCommentId));
    }

    @DeleteMapping("{commentId}")
    public ResponseEntity<ResponseObject> deleteComment(@PathVariable(name = "commentId") String commentId,
                                                        @RequestHeader("Authorization") String token) {

        newsService.deleteCommentById(commentId, token);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK.value(), "DELETE COMMENT SUCCESS", null));
    }
}
