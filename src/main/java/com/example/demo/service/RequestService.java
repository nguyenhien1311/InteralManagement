package com.example.demo.service;

import com.example.demo.domain.Request;
import com.example.demo.request.requests.CreateLateSoonRequest;
import com.example.demo.request.requests.CreateLeaveRequest;
import com.example.demo.request.user.UpdateUserRequest;
import com.example.demo.response.requests.ListRequestResponse;
import com.example.demo.response.requests.RequestByEmailResponse;
import com.example.demo.response.requests.RequestResponse;
import com.example.demo.response.requests.UpdateStatusRequest;

import java.util.List;

public interface RequestService {
    List<RequestResponse> findAll();
    RequestResponse insertLateSoonRequest(String userId,CreateLateSoonRequest request);
    RequestResponse insertLeaveRequest(String userId,CreateLeaveRequest request);
    ListRequestResponse approveRequest(String requestId, UpdateStatusRequest status, String token);
    Request findById(String id);
    List<RequestResponse> findListRequestPending(String token);
    List<RequestByEmailResponse> findListRequestByUserId(String id);

    List<RequestByEmailResponse> findListRequestByReceiverEmail(String id);
}
