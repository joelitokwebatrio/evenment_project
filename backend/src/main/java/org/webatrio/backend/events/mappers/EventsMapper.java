package org.webatrio.backend.events.mappers;

import org.mapstruct.Mapper;
import org.webatrio.backend.events.dto.EventDTO;
import org.webatrio.backend.events.models.Event;

@Mapper(componentModel = "spring")
public interface EventsMapper {
    Event mapEventDTOToEvent(EventDTO eventDTO);

    EventDTO mapEventToEventDTO(Event event);

}
