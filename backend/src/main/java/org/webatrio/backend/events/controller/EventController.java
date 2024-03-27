package org.webatrio.backend.events.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.webatrio.backend.events.dto.EventDTO;
import org.webatrio.backend.events.exceptions.EventAlreadyExistException;
import org.webatrio.backend.events.exceptions.EventErrorException;
import org.webatrio.backend.events.exceptions.EventNotFoundException;
import org.webatrio.backend.events.services.eventservice.EventService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/event")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class EventController {
    private final EventService eventService;

    @PostMapping(path = "/addEvent")
    @PreAuthorize("hasRole('ORGANIZER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<EventDTO> createEvent(@RequestBody EventDTO eventDTO) throws EventAlreadyExistException {
        EventDTO addEventDTO = eventService.addEvent(eventDTO);
        return new ResponseEntity<>(addEventDTO, HttpStatus.CREATED);
    }

    @PatchMapping(path = "/updateEvent")
    @PreAuthorize("hasRole('ORGANIZER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<EventDTO> updateEvent(@RequestBody EventDTO eventDTO) throws EventNotFoundException, EventErrorException {
        EventDTO updateEventDTO = eventService.updateEvent(eventDTO);
        return ResponseEntity.ok(updateEventDTO);
    }

    @PostMapping(path = "/cancelEvent/{eventTitle}")
    @PreAuthorize("hasRole('ORGANIZER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<EventDTO> cancelEvent(@PathParam("eventTitle") String eventTitle) throws EventNotFoundException {
        EventDTO cancelEventDTO = eventService.cancelEvent(eventTitle);
        return ResponseEntity.ok(cancelEventDTO);
    }

    @GetMapping(path = "/event/{idEvent}")
    @PreAuthorize("hasRole('ORGANIZER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Optional<EventDTO>> getEvent(@PathVariable("idEvent") Long idEvent) {
        return ResponseEntity.ok(eventService.getEvent(idEvent));
    }

    @DeleteMapping(path = "/event/{idEvent}")
    @PreAuthorize("hasRole('ORGANIZER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Void> deleteEvent(@PathVariable("idEvent") Long idEvent) {
        eventService.deleteEvent(idEvent);
        return ResponseEntity.ok().build();
    }


    @GetMapping(path = "/getAllEvents")
    @PreAuthorize("hasRole('ORGANIZER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<List<EventDTO>> getAllEvent(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "location", defaultValue = "", required = false) String location) {
        return new ResponseEntity<>(eventService.getEvents(pageNo, pageSize, location), HttpStatus.OK);
    }
}
