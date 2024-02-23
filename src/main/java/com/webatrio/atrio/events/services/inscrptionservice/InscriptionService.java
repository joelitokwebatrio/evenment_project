package com.webatrio.atrio.events.services.inscrptionservice;

import com.webatrio.atrio.events.exceptions.EventErrorException;
import com.webatrio.atrio.events.exceptions.EventNotFoundException;
import com.webatrio.atrio.events.exceptions.ParticipantNotFoundException;
import com.webatrio.atrio.security.models.participant.Participant;

import java.util.List;

public interface InscriptionService {
    void addParticipantToEvent(String eventTitle, String participantDTO) throws EventNotFoundException, ParticipantNotFoundException, EventErrorException;

    void cancelParticipantToEvent(String emailParticipant, String titleEvent);

    List<Participant> getAllParticipantByEvents(String titleEvent);
}
