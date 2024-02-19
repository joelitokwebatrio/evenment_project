package org.webatrio.eventsapp.services.participantservice;

import org.webatrio.eventsapp.dto.EventDTO;
import org.webatrio.eventsapp.dto.ParticipantDTO;
import org.webatrio.eventsapp.exceptions.ParticipantAlreadyExistException;

import java.util.List;

public interface ParticipantService {

    ParticipantDTO addParticipant(ParticipantDTO participantDTO) throws ParticipantAlreadyExistException;
    List<EventDTO> getParticipantEvent(long idParticipant);
}
