package com.example.demo.controller;

import com.example.demo.constant.EventStatus;
import com.example.demo.request.comment.CreateCommentRequest;
import com.example.demo.request.event.CreateEventRequest;
import com.example.demo.request.event.UpdateEventRequest;
import com.example.demo.response.event.EventIncomingResponse;
import com.example.demo.response.event.EventResponse;
import com.example.demo.response.event.EventSearchResponse;
import com.example.demo.response.ResponseObject;
import com.example.demo.service.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("demo/v1/events/")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("{id}")
    public ResponseEntity<ResponseObject> getEventById(@PathVariable(name = "id") String id) {
        EventResponse result = eventService.getEventById(id);
        return ResponseEntity.ok(new ResponseObject(HttpStatus.OK.value(),"Find event with success",result));
    }
    @GetMapping("incoming")
    public ResponseEntity<ResponseObject> getIncomingEvents() {
        List<EventIncomingResponse> result = eventService.getIncomingEvent();
        return ResponseEntity.ok(new ResponseObject(HttpStatus.OK.value(),"Find event with success",result));
    }
    @GetMapping("tree")
    public ResponseEntity<ResponseObject> getEventsInYear() {
        List<EventSearchResponse> result = eventService.listEventInYear();
        return ResponseEntity.ok(new ResponseObject(HttpStatus.OK.value(),"Find event with success",result));
    }

    @PostMapping
    public ResponseEntity<ResponseObject> createEvent(@RequestHeader("Authorization") String token,
                                                      @Valid @RequestBody CreateEventRequest createEventRequest) {
        EventResponse response = eventService.insert(token, createEventRequest);
        return ResponseEntity.ok(new ResponseObject(HttpStatus.OK.value(), "Create event success", response));
    }
    @PutMapping("{id}")
    public ResponseEntity<ResponseObject> updateEvent(@PathVariable(name = "id") String id,
                                                      @RequestHeader("Authorization") String token,
                                                      @Valid @RequestBody UpdateEventRequest updateEventRequest) {
        EventResponse result = eventService.save(id, token, updateEventRequest);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK.value(), "Update event successful", result));
    }
    @DeleteMapping("{id}")
    public ResponseEntity<ResponseObject> deleteEventById(@PathVariable(name = "id") String id,
                                                          @RequestHeader("Authorization") String token){
        eventService.deleteEventById(id, token);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK.value(),"Delete event successful", null));
    }

    @PostMapping("{id}/comment")
    public ResponseEntity<ResponseObject> addCommentToEvent(@PathVariable(name = "id") String id,
                                                            @RequestHeader("Authorization") String token,
                                                            @Valid @RequestBody CreateCommentRequest createCommentRequest){
        EventResponse response = eventService.addCommentToEvent(id, token, createCommentRequest);
        return ResponseEntity.ok(new ResponseObject(HttpStatus.OK.value(),"Add comment to Entity success",
                response ));
    }
    @DeleteMapping("{commentId}/comment")
    public ResponseEntity<ResponseObject> deleteComment(@PathVariable(name = "commentId") String commentId,
                                                        @RequestHeader("Authorization") String token){
        eventService.deleteCommentById(commentId,token);
        return ResponseEntity.ok(new ResponseObject(HttpStatus.OK.value(),"Delete comment success", null));
    }

    @GetMapping()
    public ResponseEntity<ResponseObject> listByEventStatus(@RequestParam(required = false,defaultValue = "0") int pageNum,
                                                            @RequestParam(required = false,defaultValue = "24") int pageSize,
                                                            @RequestParam(required = false) EventStatus eventStatus) {
        if (eventStatus == null){
            List<EventResponse> list = eventService.getList();
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK.value(), "Get list event ", list));
        }
        List<EventSearchResponse> page = eventService.listEventByStatus(pageNum, pageSize, eventStatus);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK.value(), "Get list event with status " +eventStatus.name(), page));
    }
}
