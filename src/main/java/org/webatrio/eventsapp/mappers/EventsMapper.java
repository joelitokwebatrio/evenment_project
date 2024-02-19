package org.webatrio.eventsapp.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.webatrio.eventsapp.dto.EventDTO;
import org.webatrio.eventsapp.models.Event;

@Mapper(componentModel = "spring", uses = {ParticipantMapper.class})
public interface EventsMapper {

    @Mapping(target = "participants",source = "participantDTOS")
    Event mapEventDTOToEvent(EventDTO eventDTO);

    @Mapping(target = "participantDTOS",source = "participants")
    EventDTO mapEventToEventDTO(Event event);

}
