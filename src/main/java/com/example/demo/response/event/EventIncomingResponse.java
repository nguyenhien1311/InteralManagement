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
public class EventIncomingResponse {
    private String id;
    private String title;
    private String banner;
    private String timeEnd;
    private String timeBegin;
}
