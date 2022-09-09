package com.example.demo.response.event;

import com.example.demo.constant.EventStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventResponse {
    private String id;
    private String banner;
    private String title;
    private String content;
    private EventStatus eventStatus;
    private String timeBegin;
    private String timeEnd;
}
