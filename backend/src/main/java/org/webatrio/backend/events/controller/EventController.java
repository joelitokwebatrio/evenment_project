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

    @PutMapping(path = "/updateEvent")
    @PreAuthorize("hasRole('ORGANIZER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<EventDTO> updateEvent(@RequestBody EventDTO eventDTO) throws EventNotFoundException, EventErrorException {
        EventDTO updateEventDTO = eventService.updateEvent(eventDTO);
        return ResponseEntity.ok(updateEventDTO);
    }


    @GetMapping(path = "/{idEvent}")
    //@PreAuthorize("hasRole('ORGANIZER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<EventDTO> getEvent(@PathVariable("idEvent") Long idEvent) {
        return ResponseEntity.ok(eventService.getEvent(idEvent).orElse(null));
    }


    @DeleteMapping(path = "/{idEvent}")
    @PreAuthorize("hasRole('ORGANIZER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Void> deleteEvent(@PathVariable("idEvent") Long idEvent) {
        eventService.deleteEvent(idEvent);
        return ResponseEntity.ok().build();
    }


    @GetMapping(path = "/getAllEvents")
    //@PreAuthorize("hasRole('ORGANIZER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<List<EventDTO>> getAllEvent(@RequestParam(value = "location", defaultValue = "", required = false) String location) {
        return new ResponseEntity<>(eventService.getEvents(location), HttpStatus.OK);
    }
    @GetMapping(path = "/getAllEvents/{idParticipant}")
    //@PreAuthorize("hasRole('ORGANIZER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<List<EventDTO>> getAllEventByIdParticipant( @PathVariable Integer idParticipant) {
        return new ResponseEntity<>(eventService.getEventByIdParticipant(idParticipant), HttpStatus.OK);
    }



}
