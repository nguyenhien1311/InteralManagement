package com.example.demo.service;

import com.example.demo.constant.EventStatus;
import com.example.demo.request.comment.CreateCommentRequest;
import com.example.demo.request.event.CreateEventRequest;
import com.example.demo.request.event.UpdateEventRequest;
import com.example.demo.response.event.EventIncomingResponse;
import com.example.demo.response.event.EventResponse;
import com.example.demo.response.event.EventSearchResponse;

import java.util.List;

public interface EventService {

    List<EventResponse> getList();

    EventResponse getEventById(String id);

    EventResponse insert(String token, CreateEventRequest createEventRequest);

    EventResponse save(String id, String token, UpdateEventRequest updateEventRequest);

    void deleteEventById(String id, String token);

    EventResponse findByTitleLike(String title);

    List<EventIncomingResponse> getIncomingEvent();

    EventResponse addCommentToEvent(String eventId, String token, CreateCommentRequest createCommentRequest);

    void deleteCommentById(String commentId, String token);

    List<EventSearchResponse> listEventByStatus(int pageNum, int pageSize, EventStatus eventStatus);
    List<EventSearchResponse> listEventInYear();

}
