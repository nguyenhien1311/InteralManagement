package com.example.demo.repository;

import com.example.demo.constant.EventStatus;
import com.example.demo.domain.Event;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface EventRepository extends MongoRepository<Event,String> {
    Event findByTitleLike(String title);

    List<Event> findEventsByEventStatus(EventStatus eventStatus);
}
