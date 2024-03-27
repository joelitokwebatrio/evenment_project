package org.webatrio.backend.events.controller;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.webatrio.backend.events.exceptions.EventErrorException;
import org.webatrio.backend.events.exceptions.EventNotFoundException;
import org.webatrio.backend.events.exceptions.ParticipantNotFoundException;
import org.webatrio.backend.events.models.EP;
import org.webatrio.backend.events.services.inscriptionservice.InscriptionService;
import org.webatrio.backend.security.models.participant.Participant;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(path = "/api/v1/inscription")
@RequiredArgsConstructor
public class InscriptionController {
    private final InscriptionService inscriptionService;

    @PostMapping(path = "/events")
    ResponseEntity<Void> addParticipantToEventC(@RequestBody EP ep)
            throws EventNotFoundException, ParticipantNotFoundException, EventErrorException {
        if (Objects.nonNull(ep) && Strings.isNotBlank(ep.getEventTitle()) && Strings.isNotBlank(ep.getParticipantEmail())) {
            inscriptionService.addParticipantToEvent(ep.getEventTitle(), ep.getParticipantEmail());
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/event/cancelParticipant")
    ResponseEntity<Void> cancelParticipantToEventC(@RequestParam(required = true) String emailParticipant, @RequestParam(required = true) String titleEvent) {
        inscriptionService.cancelParticipantToEvent(emailParticipant, titleEvent);
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/event/{eventTitle}")
    ResponseEntity<List<Participant>> getAllParticipantByEventsC(@RequestParam(required = true) String eventTitle) {
        return new ResponseEntity<>(inscriptionService.getAllParticipantByEvents(eventTitle), HttpStatus.OK);
    }

}
