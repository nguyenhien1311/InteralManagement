package com.example.demo.service.impl;

import com.example.demo.constant.RequestStatus;
import com.example.demo.constant.RequestType;
import com.example.demo.converter.DateTimeConvert;
import com.example.demo.converter.RequestChecker;
import com.example.demo.converter.RequestConverter;
import com.example.demo.domain.Request;
import com.example.demo.domain.User;
import com.example.demo.exception.CustomException;
import com.example.demo.repository.RequestRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.request.requests.CreateLateSoonRequest;
import com.example.demo.request.requests.CreateLeaveRequest;
import com.example.demo.response.requests.ListRequestResponse;
import com.example.demo.response.requests.RequestByEmailResponse;
import com.example.demo.response.requests.RequestResponse;
import com.example.demo.response.ResponseObject;
import com.example.demo.response.requests.UpdateStatusRequest;
import com.example.demo.service.JwtService;
import com.example.demo.service.RequestService;
import com.example.demo.util.JwtData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Override
    public List<RequestResponse> findAll() {
        List<RequestResponse> result = requestRepository.findAll()
                .stream()
                .map(request -> RequestResponse.builder()
                        .id(request.getId())
                        .requestTitle(request.getRequestTitle())
                        .requestStatus(request.getRequestStatus())
                        .timeRequest(request.getDayRequest())
                        .timeNeed(RequestConverter.getTimeNeed(request))
                        .build())
                .collect(Collectors.toList());
        return result;
    }

    @Override
    public RequestResponse insertLateSoonRequest(String userId, CreateLateSoonRequest lateSoonRequest) {
        boolean userByEmail = userRepository.existsUserByEmail(lateSoonRequest.getReceiverEmail());
        if (!userByEmail) {
            throw new CustomException(ResponseObject.STATUS_CODE_NOT_FOUND, ResponseObject.MESSAGE_USER_NOT_FOUND_WITH_EMAIL + lateSoonRequest.getReceiverEmail());
        }
        RequestChecker.checkCreateLateSoonRequest(lateSoonRequest);
        Request request = requestRepository.findFirstByUserIdOrderByIdDesc(userId);
        Request requestToInsert;
        if (request == null) {
            requestToInsert = RequestConverter.fromLateSoonResponse(userId, lateSoonRequest);
        } else {
            requestToInsert = RequestConverter.fromLateSoonResponse(userId, lateSoonRequest, request.getDayRequest(), request.getTimeRemainInWeek());
        }
        RequestConverter.checkSameDay(lateSoonRequest.getTimeLateOrSoon(), request != null ? request.getTimeNeed() : null);
        if (requestToInsert.getTimeRemainInWeek() < 0) {
            throw new CustomException(ResponseObject.STATUS_CODE_NOT_ACCEPTABLE, ResponseObject.MESSAGE_NO_REMAINING_FOR_CREATE_LATESOON_REQUEST);
        }
        Request insert = requestRepository.insert(requestToInsert);
        RequestResponse response = RequestConverter.toResponseFromLate(insert, insert.getTimeRemainInWeek());
        return response;
    }

    @Override
    public RequestResponse insertLeaveRequest(String userId, CreateLeaveRequest leaveRequest) {
        boolean userByEmail = userRepository.existsUserByEmail(leaveRequest.getReceiverEmail());
        if (!userByEmail) {
            throw new CustomException(ResponseObject.STATUS_CODE_NOT_FOUND, ResponseObject.MESSAGE_USER_NOT_FOUND_WITH_EMAIL + leaveRequest.getReceiverEmail());
        }
        RequestChecker.checkCreateLeaveRequest(leaveRequest);
        Request requestToInsert = RequestConverter.fromOffResponse(userId, leaveRequest);
        List<Request> requests = requestRepository.findRequestsByYearRequestAndRequestTypeAndUserId(DateTimeConvert.getCurrentYear(), RequestType.OFF, userId);
        int sum = requests.stream().mapToInt(request -> (int) request.getTotalDayOff()).sum();
        double total = sum + requestToInsert.getTotalDayOff();
        if (total > 180) {
            throw new CustomException(ResponseObject.STATUS_CODE_NOT_ACCEPTABLE, ResponseObject.MESSAGE_NO_REMAINING_DAY_FOR_OFF_REQUEST);
        }
        Request insert = requestRepository.insert(requestToInsert);
        RequestResponse response = RequestConverter.toResponseFromOff(insert, 180 - total);
        return response;
    }

    @Override
    public ListRequestResponse approveRequest(String requestId, UpdateStatusRequest status, String token) {
        JwtData jwtData = jwtService.parseToken(token);
        String email = jwtData.getEmail();
        Optional<Request> optionalRequest = requestRepository.findRequestsByIdAndReceiverEmail(requestId, email);
        if (optionalRequest.isEmpty()) {
            throw new CustomException(ResponseObject.STATUS_CODE_NOT_FOUND, ResponseObject.MESSAGE_REQUEST_NOT_FOUND + requestId);
        }
        Request request = optionalRequest.get();
        request.setRequestStatus(getStatusFromString(status.getStatus()));
        if (status.getStatus().equals(RequestStatus.REJECTED) && request.getRequestType().equals(RequestType.LATE_SOON)) {
            request.setTimeRemainInWeek(1);
        }
        Request save = requestRepository.save(request);
        ListRequestResponse response = RequestConverter.toResponse(save);
        return response;
    }

    @Override
    public Request findById(String id) {
        return null;
    }

    @Override
    public List<RequestResponse> findListRequestPending(String token) {
        JwtData jwtData = jwtService.parseToken(token);
        String email = jwtData.getEmail();
        List<RequestResponse> collect = requestRepository.findAllByRequestStatusAndReceiverEmail(RequestStatus.PENDING, email)
                .stream()
                .map(request -> RequestResponse.builder()
                        .id(request.getId())
                        .requestTitle(request.getRequestTitle())
                        .requestStatus(request.getRequestStatus())
                        .timeRequest(request.getDayRequest())
                        .timeNeed(RequestConverter.getTimeNeed(request))
                        .build())
                .collect(Collectors.toList());
        return collect;
    }

    @Override
    public List<RequestByEmailResponse> findListRequestByUserId(String id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new CustomException(ResponseObject.STATUS_CODE_NOT_FOUND, ResponseObject.MESSAGE_USER_NOT_FOUND + id);
        }
        User user = optionalUser.get();
        List<RequestByEmailResponse> collect = requestRepository.findRequestsByUserIdOrderByCreatedAtDesc(id)
                .stream()
                .map(request ->
                        RequestByEmailResponse.builder()
                                .id(request.getId())
                                .status(request.getRequestStatus())
                                .requestTitle(request.getRequestTitle())
                                .timeRequest(request.getDayRequest())
                                .content(request.getContent())
                                .userName(user.getName())
                                .timeNeed(RequestConverter.getTimeNeed(request))
                                .build())
                .collect(Collectors.toList());
        return collect;
    }

    @Override
    public List<RequestByEmailResponse> findListRequestByReceiverEmail(String id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new CustomException(ResponseObject.STATUS_CODE_NOT_FOUND, ResponseObject.MESSAGE_USER_NOT_FOUND + id);
        }
        User user = optionalUser.get();
        List<RequestByEmailResponse> collect = requestRepository.findRequestsByReceiverEmailAndRequestStatus(user.getEmail(), RequestStatus.PENDING)
                .stream()
                .map(request -> {
                    User requestCreator = userRepository.findUserById(request.getUserId());
                    return RequestByEmailResponse.builder()
                            .id(request.getId())
                            .status(request.getRequestStatus())
                            .requestTitle(request.getRequestTitle())
                            .timeRequest(request.getDayRequest())
                            .content(request.getContent())
                            .userName(requestCreator.getName())
                            .timeNeed(RequestConverter.getTimeNeed(request))
                            .build();
                })
                .collect(Collectors.toList());
        return collect;
    }

    public static RequestStatus getStatusFromString(String status) {
        if (RequestStatus.REJECTED.toString().equalsIgnoreCase(status)) {
            return RequestStatus.REJECTED;
        }
        return RequestStatus.APPROVED;
    }
}
