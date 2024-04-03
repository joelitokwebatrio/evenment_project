package org.webatrio.backend.events.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.webatrio.backend.events.exceptions.EventErrorException;
import org.webatrio.backend.events.exceptions.EventNotFoundException;
import org.webatrio.backend.events.exceptions.ParticipantNotFoundException;
import org.webatrio.backend.events.services.inscriptionservice.InscriptionService;
import org.webatrio.backend.security.models.participant.Participant;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/inscription")
@RequiredArgsConstructor
public class InscriptionController {
    private final InscriptionService inscriptionService;

    @PostMapping(path = "/events/{idEvent}/{idParticipant}")
    //@PreAuthorize("hasRole('ORGANIZER')")
    @SecurityRequirement(name = "Bearer Authentication")
    ResponseEntity<Void> addParticipantToEvent(@PathVariable Long idEvent, @PathVariable Integer idParticipant)
            throws EventNotFoundException, ParticipantNotFoundException, EventErrorException {
        System.out.println("value "+idEvent+" value"+idParticipant);
        inscriptionService.addParticipantToEvent(idEvent, idParticipant);
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/event/cancelParticipant/{idEvent}/{idParticipant}")
    //@PreAuthorize("hasRole('ORGANIZER')")
    @SecurityRequirement(name = "Bearer Authentication")
    ResponseEntity<Void> cancelParticipantToEvent(@PathVariable Long idEvent, @PathVariable Integer idParticipant) {
        inscriptionService.cancelParticipantToEvent(idEvent, idParticipant);
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/event/{idEvent}")
    @PreAuthorize("hasRole('ORGANIZER')")
    @SecurityRequirement(name = "Bearer Authentication")
    ResponseEntity<List<Participant>> getAllParticipantByEventsC(@PathVariable Long idEvent) {
        System.out.println(idEvent);
        return new ResponseEntity<>(inscriptionService.getAllParticipantByEvents(idEvent), HttpStatus.OK);
    }


}
