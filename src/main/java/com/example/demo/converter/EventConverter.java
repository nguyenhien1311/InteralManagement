package com.example.demo.converter;

import com.example.demo.constant.EventStatus;
import com.example.demo.domain.Event;
import com.example.demo.request.event.CreateEventRequest;
import com.example.demo.response.event.EventIncomingResponse;
import com.example.demo.response.event.EventResponse;
import com.example.demo.response.event.EventSearchResponse;

public class EventConverter {
    public static EventResponse convertToResponse(Event event) {
        return EventResponse.builder()
                .id(event.getId())
                .title(event.getTitle())
                .banner(event.getBanner())
                .eventStatus(DateTimeConvert.isEventOver(event.getTimeBegin(),event.getTimeEnd()))
                .content(event.getContent())
                .timeBegin(DateTimeConvert.convertLongToDate(event.getTimeBegin()))
                .timeEnd(DateTimeConvert.convertLongToDate(event.getTimeEnd()))
                .build();
    }
    public static EventIncomingResponse convertToIncomingResponse(Event event) {
        return EventIncomingResponse.builder()
                .id(event.getId())
                .title(event.getTitle())
                .banner(event.getBanner())
                .timeBegin(DateTimeConvert.convertLongToDate(event.getTimeBegin()))
                .timeEnd(DateTimeConvert.convertLongToDate(event.getTimeEnd()))
                .build();
    }

    public static EventSearchResponse convertToSearchResponse(Event event) {
        return EventSearchResponse.builder()
                .id(event.getId())
                .title(event.getTitle())
                .eventStatus(DateTimeConvert.isEventOver(event.getTimeBegin(),event.getTimeEnd()))
                .timeBegin(DateTimeConvert.convertLongToDate(event.getTimeBegin()))
                .build();
    }

    public static Event convertToEvent(String userId, CreateEventRequest createEventRequest) {
        long start = DateTimeConvert.fromStringToMillis(createEventRequest.getTimeBegin());
        long end = DateTimeConvert.fromStringToMillis(createEventRequest.getTimeEnd());
        EventStatus status = DateTimeConvert.isEventOver(start, end);
        return Event.builder()
                .title(createEventRequest.getTitle())
                .banner(createEventRequest.getBanner())
                .eventStatus(status)
                .content(createEventRequest.getContent())
                .timeBegin(DateTimeConvert.fromStringToMillis(createEventRequest.getTimeBegin()))
                .timeEnd(DateTimeConvert.fromStringToMillis(createEventRequest.getTimeEnd()))
                .createUserId(userId)
                .createTime(System.currentTimeMillis())
                .build();
    }
}
