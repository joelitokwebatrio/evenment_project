package org.webatrio.eventsapp.controller;

import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.webatrio.eventsapp.dto.EventDTO;
import org.webatrio.eventsapp.exceptions.EventAlreadyExistException;
import org.webatrio.eventsapp.exceptions.EventErrorException;
import org.webatrio.eventsapp.exceptions.EventNotFoundException;
import org.webatrio.eventsapp.services.eventservice.EventService;

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
    public ResponseEntity<EventDTO> updateEvent(@RequestBody EventDTO eventDTO) throws EventErrorException, EventNotFoundException {
        EventDTO updateEventDTO = eventService.updateEvent(eventDTO);
        return ResponseEntity.ok(updateEventDTO);
    }

    @PostMapping(path = "/cancelEvent/{eventTitle}")
    public ResponseEntity<EventDTO> cancelEvent(@PathParam("eventTitle") String eventTitle) throws  EventNotFoundException {
        EventDTO cancelEventDTO = eventService.cancelEvent(eventTitle);
        return ResponseEntity.ok(cancelEventDTO);
    }

    @GetMapping(path = "/getAllEvents")
    public ResponseEntity<List<EventDTO>> getAllEvent(@RequestParam(required = false) String location){
        return new ResponseEntity<>(eventService.getEvents(location), HttpStatus.OK);
    }
}
