package org.webatrio.backend.events.services.inscriptionservice;

import org.webatrio.backend.events.exceptions.EventErrorException;
import org.webatrio.backend.events.exceptions.EventNotFoundException;
import org.webatrio.backend.events.exceptions.ParticipantNotFoundException;
import org.webatrio.backend.security.models.participant.Participant;

import java.util.List;

public interface InscriptionService {
    void addParticipantToEvent(String eventTitle, String participantDTO) throws EventNotFoundException, ParticipantNotFoundException, EventErrorException;

    void cancelParticipantToEvent(String emailParticipant, String titleEvent);

    List<Participant> getAllParticipantByEvents(String titleEvent);
}
