package org.webatrio.backend.events.mappers;

import  org.webatrio.backend.events.dto.EventDTO;
import  org.webatrio.backend.events.models.Event;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
class EventsMapperTest {
    private final EventsMapper eventsMapper = new EventsMapperImpl();

    @Test
    void mapEventDTOToEvent() {

        //given
        EventDTO eventDTOMapper = getEventDTO();

        //when
        Event event = eventsMapper.mapEventDTOToEvent(eventDTOMapper);

        //then
        Assertions.assertThat(eventDTOMapper.getTitle()).isEqualTo(event.getTitle());
        Assertions.assertThat(eventDTOMapper.getDescription()).isEqualTo(event.getDescription());
        Assertions.assertThat(eventDTOMapper.getPlace()).isEqualTo(event.getPlace());
        Assertions.assertThat(eventDTOMapper.getStartEventDate()).isEqualTo(event.getStartEventDate());
        Assertions.assertThat(eventDTOMapper.getEndEventDate()).isEqualTo(event.getEndEventDate());
        Assertions.assertThat(eventDTOMapper.getNumberOfParticipants()).isEqualTo(event.getNumberOfParticipants());
        Assertions.assertThat(eventDTOMapper.getOrganiserName()).isEqualTo(event.getOrganiserName());
    }
    @Test
    void mapNullEventDTOTNull() {

        //given
        EventDTO eventDTOMapper = null;

        //when
        Event event = eventsMapper.mapEventDTOToEvent(eventDTOMapper);

        //then
        Assertions.assertThat(event).isNull();
      }

    @Test
    void mapEventToEventDTO() {
        //given
        Event event = getEvent();

        //when
        EventDTO eventDTO = eventsMapper.mapEventToEventDTO(event);

        //then
        Assertions.assertThat(event.getTitle()).isEqualTo(eventDTO.getTitle());
        Assertions.assertThat(event.getDescription()).isEqualTo(eventDTO.getDescription());
        Assertions.assertThat(event.getPlace()).isEqualTo(eventDTO.getPlace());
        Assertions.assertThat(event.getStartEventDate()).isEqualTo(eventDTO.getStartEventDate());
        Assertions.assertThat(event.getEndEventDate()).isEqualTo(eventDTO.getEndEventDate());
        Assertions.assertThat(event.getNumberOfParticipants()).isEqualTo(eventDTO.getNumberOfParticipants());
        Assertions.assertThat(event.getOrganiserName()).isEqualTo(eventDTO.getOrganiserName());
    }

    @Test
    void mapNullEventToNullEventDTO() {
        //given
        Event event = null;

        //when
        EventDTO eventDTO = eventsMapper.mapEventToEventDTO(event);

        //then
        Assertions.assertThat(event).isNull();
    }

    private EventDTO getEventDTO() {
        return EventDTO.builder()
                .title("noel")
                .description("pour tout les nouveaux")
                .startEventDate(LocalDateTime.of(LocalDate.of(1995, 11, 11), LocalTime.of(11, 11)))
                .endEventDate(LocalDateTime.of(LocalDate.of(2000, 11, 11), LocalTime.of(10, 10)))
                .place("bordeaux")
                .numberOfParticipants(47)
                .organiserName("joel")
                .status(true)
                .build();
    }

    private Event getEvent() {
        return Event.builder()
                .title("noel")
                .description("pour tout les nouveaux")
                .startEventDate(LocalDateTime.of(LocalDate.of(1995, 11, 11), LocalTime.of(11, 11)))
                .endEventDate(LocalDateTime.of(LocalDate.of(2000, 11, 11), LocalTime.of(10, 10)))
                .place("bordeaux")
                .numberOfParticipants(47)
                .organiserName("joel")
                .status(true)
                .build();
    }
}