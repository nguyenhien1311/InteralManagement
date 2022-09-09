package com.example.demo.converter;

import com.example.demo.constant.RequestStatus;
import com.example.demo.constant.RequestType;
import com.example.demo.domain.Request;
import com.example.demo.exception.CustomException;
import com.example.demo.request.requests.CreateLateSoonRequest;
import com.example.demo.request.requests.CreateLeaveRequest;
import com.example.demo.response.ResponseObject;
import com.example.demo.response.requests.ListRequestResponse;
import com.example.demo.response.requests.RequestResponse;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;

public class RequestConverter {
    public static Request fromOffResponse(String userId, CreateLeaveRequest leaveRequest) {
        return Request.builder()
                .requestStatus(RequestStatus.PENDING)
                .requestTitle(leaveRequest.getOffType().getTitle())
                .receiverEmail(leaveRequest.getReceiverEmail())
                .createdAt(System.currentTimeMillis())
                .content(leaveRequest.getContent())
                .dayRequest(leaveRequest.getDayStart() + " - " + leaveRequest.getDayEnd())
                .totalDayOff(DateTimeConvert.calculateDayOff(leaveRequest))
                .requestType(RequestType.OFF)
                .userId(userId)
                .yearRequest(DateTimeConvert.getCurrentYear())
                .build();
    }

    public static RequestResponse toResponseFromOff(Request request, double remain) {
        return RequestResponse.builder()
                .id(request.getId())
                .requestTitle(request.getRequestTitle())
                .requestStatus(request.getRequestStatus())
                .timeRequest(request.getDayRequest())
                .timeNeed(RequestConverter.getTimeNeed(request))
                .timeRemain(remain + " days in this year")
                .build();
    }

    public static ListRequestResponse toResponse(Request request) {
        return ListRequestResponse.builder()
                .id(request.getId())
                .requestTitle(request.getRequestTitle())
                .requestStatus(request.getRequestStatus())
                .timeRequest(request.getDayRequest())
                .timeNeed(RequestConverter.getTimeNeed(request))
                .build();
    }

    public static RequestResponse toResponseFromLate(Request request, int remain) {
        return RequestResponse.builder()
                .id(request.getId())
                .requestTitle(request.getRequestTitle())
                .requestStatus(request.getRequestStatus())
                .timeRequest(request.getDayRequest())
                .timeNeed(RequestConverter.getTimeNeed(request))
                .timeRemain(remain + " time in this week")
                .build();
    }

    public static Request fromLateSoonResponse(String userId, CreateLateSoonRequest lateSoonRequest) {
        return Request.builder()
                .requestStatus(RequestStatus.PENDING)
                .requestTitle(lateSoonRequest.getLateSoonType().getTitle())
                .receiverEmail(lateSoonRequest.getReceiverEmail())
                .createdAt(System.currentTimeMillis())
                .content(lateSoonRequest.getContent())
                .dayRequest(lateSoonRequest.getShift().getTitle() + " - " + lateSoonRequest.getDayRequest())
                .timeNeed(lateSoonRequest.getTimeLateOrSoon())
                .requestType(RequestType.LATE_SOON)
                .userId(userId)
                .timeRemainInWeek(1)
                .yearRequest(DateTimeConvert.getCurrentYear())
                .build();
    }

    public static Request fromLateSoonResponse(String userId, CreateLateSoonRequest lateSoonRequest, String lastRequestDay, int oldRemain) {
        return Request.builder()
                .requestStatus(RequestStatus.PENDING)
                .requestTitle(lateSoonRequest.getLateSoonType().getTitle())
                .receiverEmail(lateSoonRequest.getReceiverEmail())
                .createdAt(System.currentTimeMillis())
                .content(lateSoonRequest.getContent())
                .dayRequest(lateSoonRequest.getShift().getTitle() + " - " + lateSoonRequest.getDayRequest())
                .timeNeed(lateSoonRequest.getTimeLateOrSoon())
                .requestType(RequestType.LATE_SOON)
                .userId(userId)
                .timeRemainInWeek(countTimeRemaining(lateSoonRequest.getDayRequest(), lastRequestDay, oldRemain))
                .yearRequest(DateTimeConvert.getCurrentYear())
                .build();
    }

    public static String getTimeNeed(Request request) {
        if (request.getRequestType().equals(RequestType.OFF)) {
            return request.getTotalDayOff() + " ngày";
        }
        return request.getTimeNeed();
    }

    public static int countTimeRemaining(String dayNewRequest, String dayOldRequest, int oldRemain) {
        String[] split = dayOldRequest.split(" - ");
        long millisNew = DateTimeConvert.fromStringToMillis(dayNewRequest);
        long millisOld = DateTimeConvert.fromStringToMillis(split[1]);
        Calendar calendarNew = Calendar.getInstance();
        calendarNew.setTimeInMillis(millisNew);
        Calendar calendarOld = Calendar.getInstance();
        calendarOld.setTimeInMillis(millisOld);
        int week1 = calendarNew.get(Calendar.WEEK_OF_YEAR);
        int week2 = calendarOld.get(Calendar.WEEK_OF_YEAR);
        if (week1 == week2) {
            return oldRemain - 1;
        }
        return 1;
    }

    public static void checkSameDay(String newDay, String oldDay) {
        long millis1 = DateTimeConvert.fromStringToMillis(newDay);
        long millis2 = DateTimeConvert.fromStringToMillis(oldDay);

        LocalDate startDate = Instant.ofEpochMilli(millis1).atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate endDate = Instant.ofEpochMilli(millis2).atZone(ZoneId.systemDefault()).toLocalDate();
        double result = ChronoUnit.DAYS.between(startDate, endDate);
        if (result == 0) {
            throw new CustomException(ResponseObject.STATUS_CODE_NOT_ACCEPTABLE, ResponseObject.MESSAGE_DAY_LATE_SOON_REQUEST_DAY_DUPLICATE);
        }
    }
}
