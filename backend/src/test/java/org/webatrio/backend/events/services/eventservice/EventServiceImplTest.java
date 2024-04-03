package org.webatrio.backend.events.services.eventservice;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.context.ContextConfiguration;
import org.webatrio.backend.events.dao.EventRepository;
import org.webatrio.backend.events.dto.EventDTO;
import org.webatrio.backend.events.exceptions.EventAlreadyExistException;
import org.webatrio.backend.events.exceptions.EventNotFoundException;
import org.webatrio.backend.events.mappers.EventsMapper;
import org.webatrio.backend.events.mappers.EventsMapperImpl;
import org.webatrio.backend.events.models.Event;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.webatrio.backend.events.utils.Utils.EVENT_ALREADY_EXIST_IN_DATABASE;
import static org.webatrio.backend.events.utils.Utils.EVENT_NOT_EXIST;

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
        assertThat(saveEventDTO).isNotNull();
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
    void testGetEventDTOSByLocationAndPage() {
        //given
        List<Event> events = Collections.singletonList(getEvent());

        Mockito.when(eventRepository.findAll()).thenReturn(events);

        //when
        List<EventDTO> eventDTOS = eventService.getEvents( "bordeaux");


        //then
        assertThat(eventDTOS).isNotNull();
        assertThat(eventDTOS.size()).isEqualTo(1);
    }

    @Test
    void testGetEventsWithEmpty() {
        //given
        List<Event> events = Collections.singletonList(getEvent());
        Mockito.when(eventRepository.findAll()).thenReturn(events);

        //when
        List<EventDTO> eventDTOS = eventService.getEvents("");


        //then
        assertThat(eventDTOS).isNotNull();
        assertThat(eventDTOS.size()).isEqualTo(1);
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
    public void testCancelExistingEvent() throws Exception {
        // Mock data
        Event expectedEvent = new Event();
        expectedEvent.setTitle("Test Event");
        expectedEvent.setStatus(true);
        EventDTO expectedDTO = new EventDTO();

        // Mocked behavior
        Mockito.when(eventRepository.findAll()).thenReturn(Collections.singletonList(expectedEvent));
        Mockito.when(eventsMapper.mapEventToEventDTO(expectedEvent)).thenReturn(expectedDTO);

        // Call the method
        EventDTO eventDTO = eventService.cancelEvent("Test Event");

        // Verify interactions, event update, and returned DTO
        Mockito.verify(eventRepository).findAll();
        assertThat(expectedEvent.isStatus()).isFalse();
        Mockito.verify(eventRepository).save(expectedEvent);
        assertThat(eventDTO).isEqualTo(expectedDTO);
    }

    @Test
    public void testCancelNonExistingEvent() throws Exception {
        // Mock data (event not found)
        Mockito.when(eventRepository.findAll()).thenReturn(Collections.emptyList());

        // Call the method (expecting exception)
       Assertions.assertThatThrownBy(()->eventService.cancelEvent("Non-Existing Event"))
               .isInstanceOf(EventNotFoundException.class).hasMessage(EVENT_NOT_EXIST);


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