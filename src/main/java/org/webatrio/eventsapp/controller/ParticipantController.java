package org.webatrio.eventsapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.webatrio.eventsapp.dto.EventDTO;
import org.webatrio.eventsapp.dto.ParticipantDTO;
import org.webatrio.eventsapp.exceptions.ParticipantAlreadyExistException;
import org.webatrio.eventsapp.services.participantservice.ParticipantService;

import java.util.List;

@RestController
@RequestMapping(path = "/api/participant")
@RequiredArgsConstructor
public class ParticipantController {
    private final ParticipantService participantService;

    @PostMapping(path = "/addParticipant")
    public ResponseEntity<ParticipantDTO> createParticipant(@RequestBody ParticipantDTO participantDTO) throws ParticipantAlreadyExistException {
        ParticipantDTO addParticipantDTO = participantService.addParticipant(participantDTO);
        return new ResponseEntity<>(addParticipantDTO, HttpStatus.CREATED);
    }

    @GetMapping(path = "/events")
    public ResponseEntity<List<EventDTO>> getParticipantEvent(@RequestParam(required = false) String idParticipant){
        return new ResponseEntity<>(participantService.getParticipantEvent(Long.parseLong(idParticipant)), HttpStatus.OK);
    }

}
