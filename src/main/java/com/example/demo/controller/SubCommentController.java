package com.example.demo.controller;

import com.example.demo.response.ResponseObject;
import com.example.demo.response.subcomment.SubCommentResponse;
import com.example.demo.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("demo/v1/subcomments/")
@RequiredArgsConstructor
public class SubCommentController {
    private final NewsService newsService;

    @GetMapping()
    public ResponseEntity<ResponseObject> getListSubComment(@RequestHeader("Authorization") String token,
                                                            @RequestParam(defaultValue = "0", required = false) int skip,
                                                            @RequestParam(defaultValue = "24", required = false) int limit,
                                                            @RequestParam String commentId) {
        List<SubCommentResponse> listComment = newsService.getListSubComment(commentId, token, skip, limit);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK.value(), "GET LIST SUBCOMMENT SUCCESS", listComment));
    }

    @DeleteMapping("{subCommentId}")
    public ResponseEntity<ResponseObject> deleteSubCommentById(@PathVariable(name = "subCommentId") String subCommentId,
                                                               @RequestHeader("Authorization") String token) {
        newsService.deleteSubCommentById(subCommentId, token);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK.value(), "DELETE SUB COMMENT SUCCESS", null));
    }
}
