package com.example.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class News implements Comparable {
    @Id
    private String id;
    private String createUserId;
    private String updateUserId;
    private String banner;
    private String title;
    private String content;
    private Long createTime;
    private Long lastUpdateTime;
    private String hashTag;
    private List<Comment> commentList = new ArrayList<>();

    @Override
    public int compareTo(Object o) {
        News news = (News) o;
        return this.getCreateTime().compareTo(news.getCreateTime());
    }
}
