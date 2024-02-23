package com.webatrio.atrio.events.controller;

import com.webatrio.atrio.events.dto.EventDTO;
import com.webatrio.atrio.events.exceptions.EventAlreadyExistException;
import com.webatrio.atrio.events.exceptions.EventErrorException;
import com.webatrio.atrio.events.exceptions.EventNotFoundException;
import com.webatrio.atrio.events.services.eventservice.EventService;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api/event")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @PostMapping(path = "/addEvent")
    public ResponseEntity<EventDTO> createEvent(@RequestBody EventDTO eventDTO) throws EventAlreadyExistException {
        EventDTO addEventDTO = eventService.addEvent(eventDTO);
        return new ResponseEntity<>(addEventDTO, HttpStatus.CREATED);
    }

    @PatchMapping(path = "/updateEvent")
    public ResponseEntity<EventDTO> updateEvent(@RequestBody EventDTO eventDTO) throws EventNotFoundException, EventErrorException {
        EventDTO updateEventDTO = eventService.updateEvent(eventDTO);
        return ResponseEntity.ok(updateEventDTO);
    }

    @PostMapping(path = "/cancelEvent/{eventTitle}")
    public ResponseEntity<EventDTO> cancelEvent(@PathParam("eventTitle") String eventTitle) throws EventNotFoundException {
        EventDTO cancelEventDTO = eventService.cancelEvent(eventTitle);
        return ResponseEntity.ok(cancelEventDTO);
    }

    @GetMapping(path = "/getAllEvents")
    public ResponseEntity<List<EventDTO>> getAllEvent(
            @RequestParam(value = "pageNo",defaultValue = "0",required = false) int pageNo,
            @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
            @RequestParam(value = "location",defaultValue = "", required = false) String location) {
        return new ResponseEntity<>(eventService.getEvents(pageNo,pageSize,location), HttpStatus.OK);
    }
}
