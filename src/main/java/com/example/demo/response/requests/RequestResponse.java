package com.example.demo.response.requests;

import com.example.demo.constant.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestResponse {
    private String id;
    private String requestTitle;
    private RequestStatus requestStatus;
    private String timeRequest;
    private String timeRemain;
    private String timeNeed;
}
