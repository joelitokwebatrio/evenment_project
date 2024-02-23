package com.webatrio.atrio.events.mappers;

import com.webatrio.atrio.events.dto.EventDTO;
import com.webatrio.atrio.events.models.Event;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EventsMapper {
    Event mapEventDTOToEvent(EventDTO eventDTO);
    EventDTO mapEventToEventDTO(Event event);

}
