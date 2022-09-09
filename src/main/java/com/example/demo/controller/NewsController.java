package com.example.demo.controller;

import com.example.demo.request.comment.CreateCommentRequest;
import com.example.demo.request.news.CreateNewsRequest;
import com.example.demo.request.news.UpdateNewsRequest;
import com.example.demo.response.*;
import com.example.demo.response.news.NewsResponse;
import com.example.demo.response.news.NewsSearchResponse;
import com.example.demo.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("demo/v1/news/")
public class NewsController {

    private final NewsService newsService;

    @GetMapping()
    public ResponseEntity<ResponseObject> listByPage(@RequestParam(defaultValue = "0", required = false) int skip,
                                                     @RequestParam(defaultValue = "24", required = false) int limit,
                                                     @RequestParam(required = false) String hashTag) {
        NewsSearchResponse page = newsService.listByPage(skip, limit, hashTag);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK.value(), "Get list pagination success!", page));
    }

    @GetMapping("{id}")
    public ResponseEntity<ResponseObject> getNewsById(@PathVariable(name = "id") String id) {
        NewsResponse news = newsService.getNewsById(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK.value(), "Get news with id " + id, news));
    }

    @PostMapping()
    public ResponseEntity<ResponseObject> createNews(@RequestHeader("Authorization") String token, @Valid @RequestBody CreateNewsRequest createNewsRequest) {
        NewsResponse news = newsService.insert(token, createNewsRequest);
        return ResponseEntity.ok(new ResponseObject(HttpStatus.OK.value(), "Create news success", news));
    }

    @PutMapping("{id}")
    public ResponseEntity<ResponseObject> updateNews(@PathVariable(name = "id") String id, @RequestHeader("Authorization") String token, @Valid @RequestBody UpdateNewsRequest updateNewsRequest) {
        NewsResponse news = newsService.save(id, token, updateNewsRequest);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK.value(), "Update news successful", news));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ResponseObject> deleteNews(@PathVariable(name = "id") String id, @RequestHeader("Authorization") String token) {
        newsService.deleteNewsById(id, token);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK.value(), "DELTED NEWS WITH ID: " + id + " SUCCESSFULL", null));
    }

    @PostMapping("{newsId}/comments")
    public ResponseEntity<ResponseObject> addCommentToNews(@RequestHeader("Authorization") String token,
                                                           @Valid @RequestBody CreateCommentRequest createCommentRequest,
                                                           @PathVariable("newsId") String newsId) {
        String commentId = newsService.addCommentToNews(newsId, token, createCommentRequest);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK.value(), "ADD COMMENT TO NEWS SUCCESS", "Comment ID :" + commentId));
    }
}