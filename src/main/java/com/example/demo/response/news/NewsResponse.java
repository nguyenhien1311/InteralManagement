package com.example.demo.response.news;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewsResponse {
    private String id;
    private String banner;
    private String title;
    private String content;
    private String hashTag;
    private String lastUpdateTime;
    private Integer commentNum;

}
