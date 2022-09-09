package com.example.demo.domain;

import com.example.demo.constant.EventStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Event {
    @Id
    private String id;
    private String createUserId;
    private String updateUserId;
    private String banner;
    private String title;
    private String content;
    private EventStatus eventStatus;
    private Long timeBegin;
    private Long timeEnd;
    private Long createTime;
    private Long lastUpdateTime;
    private List<Comment> commentList = new ArrayList<>();
}
