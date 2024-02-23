package com.webatrio.atrio.events.services.eventservice;


import com.webatrio.atrio.events.dto.EventDTO;
import com.webatrio.atrio.events.exceptions.EventAlreadyExistException;
import com.webatrio.atrio.events.exceptions.EventErrorException;
import com.webatrio.atrio.events.exceptions.EventNotFoundException;

import java.util.List;

public interface EventService {
    EventDTO addEvent(EventDTO eventDTO) throws EventAlreadyExistException;

    List<EventDTO> getEvents(int pageNo,int pageSize, String location);

    EventDTO cancelEvent(String title) throws EventNotFoundException;

    EventDTO updateEvent(EventDTO eventDTO) throws EventNotFoundException, EventErrorException;


}
