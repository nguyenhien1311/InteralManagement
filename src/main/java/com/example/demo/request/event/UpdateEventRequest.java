package com.example.demo.request.event;

import com.example.demo.constant.EventStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventRequest {
    private String banner;
    @Size(min = 30, max = 200, message = "Title length at least 30 and max is 200 character")
    private String title;
    private String content;
    private EventStatus eventStatus;
    private String timeBegin;
    private String timeEnd;
}
