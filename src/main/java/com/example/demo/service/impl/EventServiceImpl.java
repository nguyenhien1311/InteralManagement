package com.example.demo.service.impl;

import com.example.demo.constant.EventStatus;
import com.example.demo.constant.Role;
import com.example.demo.converter.DateTimeConvert;
import com.example.demo.converter.EventConverter;
import com.example.demo.domain.Event;
import com.example.demo.exception.CustomException;
import com.example.demo.repository.EventRepository;
import com.example.demo.request.comment.CreateCommentRequest;
import com.example.demo.request.event.CreateEventRequest;
import com.example.demo.request.event.UpdateEventRequest;
import com.example.demo.response.event.EventIncomingResponse;
import com.example.demo.response.event.EventResponse;
import com.example.demo.response.event.EventSearchResponse;
import com.example.demo.response.ResponseObject;
import com.example.demo.service.EventService;
import com.example.demo.service.JwtService;
import com.example.demo.util.JwtData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    private final JwtService jwtService;

    @Override
    public List<EventResponse> getList() {
        return eventRepository.findAll()
                .stream()
                .map(event ->
                        EventResponse.builder()
                                .title(event.getTitle())
                                .content(event.getContent())
                                .eventStatus(event.getEventStatus())
                                .timeEnd(DateTimeConvert.convertLongToDate(event.getTimeEnd()))
                                .timeBegin(DateTimeConvert.convertLongToDate(event.getTimeBegin()))
                                .build()
                )
                .collect(Collectors.toList());
    }

    public EventResponse getEventById(String id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> {
                    throw new CustomException(ResponseObject.STATUS_CODE_NOT_FOUND, ResponseObject.MESSAGE_EVENT_NOT_FOUND + id);
                });
        return EventConverter.convertToResponse(event);
    }

    @Override
    public EventResponse insert(String token, CreateEventRequest createEventRequest) {
        JwtData jwtData = jwtService.parseToken(token);
        Role role = jwtData.getRole();
        String userId = jwtData.getId();
        if (!role.equals(Role.ADMIN)) {
            throw new CustomException(ResponseObject.STATUS_CODE_UNAUTHORIZED, ResponseObject.MESSAGE_UNAUTHORIZED);
        }

        Event event = EventConverter.convertToEvent(userId, createEventRequest);
        Event insertedValue = eventRepository.insert(event);
        return EventConverter.convertToResponse(insertedValue);
    }

    @Override
    public EventResponse save(String id, String token, UpdateEventRequest updateEventRequest) {
        JwtData jwtData = jwtService.parseToken(token);
        String userId = jwtData.getId();
        Role role = jwtData.getRole();

        Optional<Event> optionalEvent = eventRepository.findById(id);
        if (optionalEvent.isEmpty()) {
            throw new CustomException(ResponseObject.STATUS_CODE_NOT_FOUND, ResponseObject.MESSAGE_EVENT_NOT_FOUND + id);
        }
        if (!role.equals(Role.ADMIN)) {
            throw new CustomException(ResponseObject.STATUS_CODE_UNAUTHORIZED, ResponseObject.MESSAGE_UNAUTHORIZED_TO_UPDATE_EVENT);
        }
        Event event = optionalEvent.get();
        event.setUpdateUserId(userId);
        event.setLastUpdateTime(System.currentTimeMillis());
        if (updateEventRequest.getEventStatus() != null) {
            event.setEventStatus(updateEventRequest.getEventStatus());
        }
        if (updateEventRequest.getBanner() != null) {
            event.setBanner(updateEventRequest.getBanner());
        }
        if (updateEventRequest.getTitle() != null) {
            event.setTitle(updateEventRequest.getTitle());
        }
        if (updateEventRequest.getContent() != null) {
            event.setContent(updateEventRequest.getContent());
        }
        if (updateEventRequest.getTimeBegin() != null) {
            event.setTimeBegin(DateTimeConvert.fromStringToMillis(updateEventRequest.getTimeBegin()));
        }
        if (updateEventRequest.getTimeEnd() != null) {
            event.setTimeEnd(DateTimeConvert.fromStringToMillis(updateEventRequest.getTimeEnd()));
        }
        Event save = eventRepository.save(event);
        return EventConverter.convertToResponse(save);
    }

    @Override
    public void deleteEventById(String id, String token) {
        JwtData jwtData = jwtService.parseToken(token);
        String userId = jwtData.getId();
        eventRepository.findById(id)
                .ifPresentOrElse(event1 -> {
                    if (!userId.equals(event1.getCreateUserId())) {
                        throw new CustomException(ResponseObject.STATUS_CODE_NOT_ALLOW, ResponseObject.MESSAGE_EVENT_DELETE_NOT_ALLOW);
                    }
                    eventRepository.delete(event1);
                }, () -> {
                    throw new CustomException(ResponseObject.STATUS_CODE_NOT_FOUND, ResponseObject.MESSAGE_EVENT_NOT_FOUND + id);
                });
    }


    @Override
    public EventResponse findByTitleLike(String title) {
        Event event = eventRepository.findByTitleLike(title);
        return EventConverter.convertToResponse(event);
    }

    @Override
    public EventResponse addCommentToEvent(String eventId, String token, CreateCommentRequest createCommentRequest) {
//        Optional<Event> optionalEvent = eventRepository.findById(eventId);
//        if (optionalEvent.isEmpty()) {
//            throw new CustomException(ResponseObject.STATUS_CODE_NOT_FOUND, ResponseObject.MESSAGE_NEWS_NOT_FOUND);
//        }
//        String userId = jwtService.parseTokenToId(token);
//        Comment comment = new Comment();
//        comment.setContent(createCommentRequest.getContent());
//        comment.setCreateCommentTime(System.currentTimeMillis());
//        comment.setUserId(userId);
//        comment.setContainerId(eventId);
//        commentRepository.save(comment);
//        event.getCommentList().add(comment);
//        return eventRepository.save(event);
        return null;
    }

    @Override
    public void deleteCommentById(String commentId, String token) {
//        Comment commentToDelete = commentRepository.findById(commentId).get();
//        if (commentToDelete == null) {
//            throw new CustomException(ResponseObject.STATUS_CODE_NOT_FOUND, ResponseObject.MESSAGE_OBJECT_NOT_FOUND + commentId);
//        }
//        String userDeleteId = jwtService.parseTokenToId(token);
//        Role role = Role.valueOf(jwtService.parseTokenToRole(token));
//
//        if (!userDeleteId.equals(commentToDelete.getUserId())) {
//            throw new CustomException(ResponseObject.STATUS_CODE_UNAUTHORIZED, ResponseObject.MESSAGE_UNAUTHORIZED);
//        }
//        if (!role.equals(Role.ADMIN)) {
//            throw new CustomException(ResponseObject.STATUS_CODE_UNAUTHORIZED, ResponseObject.MESSAGE_UNAUTHORIZED);
//        }
//        commentRepository.delete(commentToDelete);
    }

    @Override
    public List<EventSearchResponse> listEventByStatus(int pageNum, int pageSize, EventStatus eventStatus) {
        return eventRepository.findEventsByEventStatus(eventStatus)
                .stream()
                .skip((long) pageNum * pageSize)
                .limit(pageSize)
                .map(event -> new EventSearchResponse(event.getId(),event.getTitle(), DateTimeConvert.convertLongToDate(event.getTimeBegin()), event.getEventStatus()))
                .collect(Collectors.toList());
    }

    @Override
    public List<EventSearchResponse> listEventInYear() {
        List<Event> eventList = eventRepository.findAll()
                .stream()
                .sorted(Comparator.comparingLong(Event::getTimeBegin).reversed())
                .collect(Collectors.toList());
        List<EventSearchResponse> eventInYear = new ArrayList<>();
        for (Event e : eventList) {
            if (DateTimeConvert.checkInYear(e.getTimeBegin())) {
                eventInYear.add(EventConverter.convertToSearchResponse(e));
            }
        }
        return eventInYear;
    }

    @Override
    public List<EventIncomingResponse> getIncomingEvent() {
        return eventRepository.findEventsByEventStatus(EventStatus.INCOMMING)
                .stream()
                .sorted(Comparator.comparingLong(Event::getTimeBegin).reversed())
                .map(EventConverter::convertToIncomingResponse)
                .collect(Collectors.toList());
    }
}
