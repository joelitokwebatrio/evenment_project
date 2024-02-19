package org.webatrio.eventsapp.controller;

import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.webatrio.eventsapp.dto.ParticipantDTO;
import org.webatrio.eventsapp.exceptions.EventNotFoundException;
import org.webatrio.eventsapp.exceptions.ParticipantNotFoundException;
import org.webatrio.eventsapp.services.inscrptionservice.InscriptionService;

import java.util.List;

@RestController
@RequestMapping(path = "/api/inscription")
@RequiredArgsConstructor
public class InscriptionController {
   private final InscriptionService inscriptionService;
       @PatchMapping(path = "/events")
       ResponseEntity<Void> addParticipantToEventC(@RequestParam(required = true) String eventTitle, @RequestParam(required = true) String participantEmail)
               throws EventNotFoundException, ParticipantNotFoundException {
        inscriptionService.addParticipantToEvent(eventTitle,participantEmail);
        return ResponseEntity.ok().build();
    }

    @PatchMapping(path = "/event/cancelParticipant")
    ResponseEntity<Void> cancelParticipantToEventC(@RequestParam(required = true) String emailParticipant, @RequestParam(required = true) String titleEvent){
        inscriptionService.cancelParticipantToEvent(emailParticipant,titleEvent);
        return ResponseEntity.ok().build();
    }

    @PatchMapping(path = "/event/{eventTitle}")
    ResponseEntity<List<ParticipantDTO>> getAllParticipantByEventsC(@RequestParam(required = true) String eventTitle){
        return new ResponseEntity<>(inscriptionService.getAllParticipantByEvents(eventTitle), HttpStatus.OK);
    }


}
