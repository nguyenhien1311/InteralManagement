package com.example.demo.converter;

import com.example.demo.domain.News;
import com.example.demo.response.news.NewsResponse;

public class NewsConverter {
    public static NewsResponse convertToNewsResponse(News news,int commentsNum){
        NewsResponse newsResponse = new NewsResponse();
        newsResponse.setId(news.getId());
        newsResponse.setTitle(news.getTitle());
        newsResponse.setBanner(news.getBanner());
        newsResponse.setContent(news.getContent());
        newsResponse.setHashTag(news.getHashTag());
        newsResponse.setLastUpdateTime(DateTimeConvert.convertLongToDate(news.getLastUpdateTime()));
        newsResponse.setCommentNum(commentsNum);
        return newsResponse;
    }
}
