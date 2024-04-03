package org.webatrio.backend.events.services.inscriptionservice;

import org.webatrio.backend.events.exceptions.EventErrorException;
import org.webatrio.backend.events.exceptions.EventNotFoundException;
import org.webatrio.backend.events.exceptions.ParticipantNotFoundException;
import org.webatrio.backend.events.models.EP;
import org.webatrio.backend.security.models.participant.Participant;

import java.util.List;

public interface InscriptionService {
    void addParticipantToEvent(Long idEvent, Integer idParticipant)
            throws EventNotFoundException, ParticipantNotFoundException, EventErrorException;

    void cancelParticipantToEvent(Long idEvent,Integer idParticipant);

    List<Participant> getAllParticipantByEvents(Long idEvent);

}
