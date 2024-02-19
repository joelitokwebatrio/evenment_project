package org.webatrio.eventsapp.mappers;

import org.mapstruct.Mapper;
import org.webatrio.eventsapp.dto.ParticipantDTO;
import org.webatrio.eventsapp.models.Participant;

@Mapper(componentModel = "spring")
public interface ParticipantMapper {
ParticipantDTO mapParticipantToParticipantDTO(Participant participantDTO);
Participant mapParticipantDTOToParticipant(ParticipantDTO participantDTO);
}
