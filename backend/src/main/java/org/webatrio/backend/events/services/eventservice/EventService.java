package org.webatrio.backend.events.services.eventservice;


import org.webatrio.backend.events.dto.EventDTO;
import org.webatrio.backend.events.exceptions.EventAlreadyExistException;
import org.webatrio.backend.events.exceptions.EventErrorException;
import org.webatrio.backend.events.exceptions.EventNotFoundException;

import java.util.List;
import java.util.Optional;

public interface EventService {
    EventDTO addEvent(EventDTO eventDTO) throws EventAlreadyExistException;

    Optional<EventDTO> getEvent(Long idEvent);

    void deleteEvent(Long idEvent);

    List<EventDTO> getEvents(String location);


    EventDTO updateEvent(EventDTO eventDTO) throws EventNotFoundException, EventErrorException;

    List<EventDTO> getEventByIdParticipant(Integer id);


}
