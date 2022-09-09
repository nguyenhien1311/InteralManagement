package com.example.demo.domain;

import com.example.demo.constant.OffType;
import com.example.demo.constant.Reasons;
import com.example.demo.constant.RequestStatus;
import com.example.demo.constant.RequestType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document
public class Request {
    @Id
    private String id;
    private RequestStatus requestStatus;
    private RequestType requestType;
    private String requestTitle;
    private String content;
    private String userId;
    private String receiverEmail;
    private double totalDayOff;
    private String dayRequest;
    private String timeNeed;
    private long createdAt;
    private int timeRemainInWeek;
    private int yearRequest;
}
