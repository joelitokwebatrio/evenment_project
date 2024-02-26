package com.webatrio.atrio.events.services.eventservice;

import com.webatrio.atrio.events.dao.EventRepository;
import com.webatrio.atrio.events.dto.EventDTO;
import com.webatrio.atrio.events.exceptions.EventAlreadyExistException;
import com.webatrio.atrio.events.mappers.EventsMapper;
import com.webatrio.atrio.events.mappers.EventsMapperImpl;
import com.webatrio.atrio.events.models.Event;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.webatrio.atrio.events.utils.Utils.EVENT_ALREADY_EXIST_IN_DATABASE;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@ContextConfiguration(classes = {EventsMapperImpl.class})
class EventServiceImplTest {
    @InjectMocks
    private EventServiceImpl eventService;

    @Mock
    private EventRepository eventRepository;
    @Mock
    private EventsMapper eventsMapper;

    @Test
    void testAddEvent() throws EventAlreadyExistException {
        //given
        EventDTO eventDTO = getEventDTO();
        Event event = getEvent();

        Mockito.when(eventRepository.findEventByTitle(eventDTO.getTitle())).thenReturn(Optional.empty());
        Mockito.when(eventRepository.save(Mockito.any(Event.class))).thenReturn(event);
        Mockito.when(eventsMapper.mapEventDTOToEvent(eventDTO)).thenReturn(event);
        Mockito.when(eventsMapper.mapEventToEventDTO(event)).thenReturn(eventDTO);

        //when
        EventDTO saveEventDTO = eventService.addEvent(eventDTO);

        //then
        Assertions.assertThat(saveEventDTO).isNotNull();
    }

    @Test
    void testAddEventAlreadyExist() throws EventAlreadyExistException {
        //given
        EventDTO eventDTO = getEventDTO();
        Event eventExist = getEvent();

        //when
        Mockito.when(eventRepository.findEventByTitle(eventDTO.getTitle())).thenReturn(Optional.of(eventExist));
        Mockito.when(eventsMapper.mapEventDTOToEvent(eventDTO)).thenReturn(eventExist);
        Mockito.when(eventsMapper.mapEventToEventDTO(eventExist)).thenReturn(eventDTO);

        //then
        Assertions.assertThatThrownBy(() -> eventService.addEvent(eventDTO))
                .isInstanceOf(EventAlreadyExistException.class)
                .hasMessage(EVENT_ALREADY_EXIST_IN_DATABASE);
    }

    @Test
    void testGetEventsByLocation() {
        //given
        List<Event> events = Collections.singletonList(getEvent());
        Page event = Mockito.mock(Page.class);
        Mockito.when(eventRepository.findAll(Mockito.any(Pageable.class))).thenReturn(event);
        Mockito.when(eventRepository.findAll()).thenReturn(events);

        //when
        List<EventDTO> eventDTOS = eventService.getEvents(1,1,"bordeaux");


        //then
        Assertions.assertThat(eventDTOS).isNotNull();
        Assertions.assertThat(eventDTOS.size()).isEqualTo(0);
    }

    @Test
    void testGetEventsWithEmpty() {
        //given
        List<Event> events = Collections.singletonList(getEvent());
        Mockito.when(eventRepository.findAll()).thenReturn(events);

        //when
        List<EventDTO> eventDTOS = eventService.getEvents(1,1,null);


        //then
        Assertions.assertThat(eventDTOS).isNotNull();
        Assertions.assertThat(eventDTOS.size()).isEqualTo(1);
    }


    @Test
    void testCancelEventIfPresent() {
        //given

        //when

        //then

    }

    @Test
    void testCancelEventIfEmpty() {
        //given

        //when

        //then
    }

    @Test
    void testUpdateEvent() {
    }


    public Event getEvent() {
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

    public EventDTO getEventDTO() {
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
}