package org.webatrio.eventsapp.services.inscrptionservice;

import org.webatrio.eventsapp.dto.ParticipantDTO;
import org.webatrio.eventsapp.exceptions.EventNotFoundException;
import org.webatrio.eventsapp.exceptions.ParticipantNotFoundException;

import java.util.List;

public interface InscriptionService {
    void addParticipantToEvent(String eventTitle, String participantDTO) throws EventNotFoundException, ParticipantNotFoundException;
    void cancelParticipantToEvent(String emailParticipant,String titleEvent);
    List<ParticipantDTO> getAllParticipantByEvents(String titleEvent);
}
