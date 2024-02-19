package org.webatrio.eventsapp.services.eventservice;


import org.webatrio.eventsapp.dto.EventDTO;
import org.webatrio.eventsapp.exceptions.EventAlreadyExistException;
import org.webatrio.eventsapp.exceptions.EventErrorException;
import org.webatrio.eventsapp.exceptions.EventNotFoundException;

import java.util.List;

public interface EventService{
EventDTO addEvent(EventDTO eventDTO) throws EventAlreadyExistException;
List<EventDTO> getEvents(String location);
EventDTO cancelEvent(String title) throws EventNotFoundException;
EventDTO updateEvent(EventDTO eventDTO) throws EventNotFoundException, EventErrorException;


}
