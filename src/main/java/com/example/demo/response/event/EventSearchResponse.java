package com.example.demo.response.event;

import com.example.demo.constant.EventStatus;
import com.example.demo.domain.Event;
import com.example.demo.domain.News;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventSearchResponse {
    private String id;
    private String title;
    private String timeBegin;
    private EventStatus eventStatus;
}
