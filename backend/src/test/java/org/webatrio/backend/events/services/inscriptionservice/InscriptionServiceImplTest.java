package org.webatrio.backend.events.services.inscriptionservice;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.webatrio.backend.events.dao.EventRepository;
import org.webatrio.backend.events.exceptions.EventNotFoundException;
import org.webatrio.backend.events.exceptions.ParticipantNotFoundException;
import org.webatrio.backend.events.models.Event;
import org.webatrio.backend.events.services.eventservice.EventServiceImpl;
import org.webatrio.backend.security.dao.ParticipantRepository;
import org.webatrio.backend.security.models.participant.Participant;
import org.webatrio.backend.security.services.authservice.AuthenticationService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class InscriptionServiceImplTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private ParticipantRepository participantRepository;

    @InjectMocks
    private InscriptionServiceImpl inscriptionService;

    private static final Long EXISTING_EVENT_ID = 1L;
    private static final Integer EXISTING_PARTICIPANT_ID = 2;


    @Test
    public void testAddParticipantToExistingEvent() throws Exception {
        // Mock data
        Event expectedEvent = new Event();
        expectedEvent.setId(EXISTING_EVENT_ID);
        Participant expectedParticipant = new Participant();
        expectedParticipant.setId(EXISTING_PARTICIPANT_ID);

        // Mocked behavior
        Mockito.when(eventRepository.findById(EXISTING_EVENT_ID)).thenReturn(Optional.of(expectedEvent));
        Mockito.when(participantRepository.findById(EXISTING_PARTICIPANT_ID)).thenReturn(Optional.of(expectedParticipant));

        // Call the method
        inscriptionService.addParticipantToEvent(EXISTING_EVENT_ID, EXISTING_PARTICIPANT_ID);

        // Verify interactions and participant state
        Mockito.verify(participantRepository).findById(EXISTING_PARTICIPANT_ID);
        Mockito.verify(eventRepository).findById(EXISTING_EVENT_ID);
        Mockito.verify(participantRepository).save(expectedParticipant);

        // Assert participant has the event added
        assertThat(expectedParticipant.getEvents()).contains(expectedEvent);
        assertThat(expectedEvent.getParticipants()).contains(expectedParticipant);
    }

    @Test
    public void testAddParticipantToNonExistingEvent() throws Exception {
        // Mock data (event not found)
        Mockito.when(eventRepository.findById(EXISTING_EVENT_ID)).thenReturn(Optional.empty());
        Mockito.when(participantRepository.findById(EXISTING_PARTICIPANT_ID)).thenReturn(Optional.of(new Participant()));

        // Call the method (expecting exception)
        assertThrows(EventNotFoundException.class, () -> inscriptionService.addParticipantToEvent(EXISTING_EVENT_ID, EXISTING_PARTICIPANT_ID));
    }

    @Test
    public void testAddParticipantToNonExistingParticipant() throws Exception {
        // Mock data (participant not found)
        Mockito.when(eventRepository.findById(EXISTING_EVENT_ID)).thenReturn(Optional.of(new Event()));
        Mockito.when(participantRepository.findById(EXISTING_PARTICIPANT_ID)).thenReturn(Optional.empty());

        // Call the method (expecting exception)
        assertThrows(ParticipantNotFoundException.class, () -> inscriptionService.addParticipantToEvent(EXISTING_EVENT_ID, EXISTING_PARTICIPANT_ID));

    }

    @Test
    public void testAddParticipantToEventAlreadyRegistered() throws Exception {
        // Mock data (participant already registered)
        Event existingEvent = new Event();
        existingEvent.setId(EXISTING_EVENT_ID);
        Participant existingParticipant = new Participant();
        existingParticipant.setId(EXISTING_PARTICIPANT_ID);
        existingParticipant.getEvents().add(existingEvent);

        Mockito.when(eventRepository.findById(EXISTING_EVENT_ID)).thenReturn(Optional.of(existingEvent));
        Mockito.when(participantRepository.findById(EXISTING_PARTICIPANT_ID)).thenReturn(Optional.of(existingParticipant));

        // Call the method
        inscriptionService.addParticipantToEvent(EXISTING_EVENT_ID, EXISTING_PARTICIPANT_ID);

        // Verify no changes are saved
        Mockito.verify(participantRepository).save(existingParticipant);
    }


    @Test
    public void testCancelParticipantFromExistingEvent() throws Exception {
        // Mock data
        Event expectedEvent = new Event();
        expectedEvent.setId(EXISTING_EVENT_ID);
        Participant expectedParticipant = new Participant();
        expectedParticipant.setId(EXISTING_PARTICIPANT_ID);
        expectedParticipant.getEvents().add(expectedEvent);
        expectedEvent.getParticipants().add(expectedParticipant);

        // Mocked behavior
        Mockito.when(eventRepository.findById(EXISTING_EVENT_ID)).thenReturn(Optional.of(expectedEvent));
        Mockito.when(participantRepository.findById(EXISTING_PARTICIPANT_ID)).thenReturn(Optional.of(expectedParticipant));

        // Call the method
        inscriptionService.cancelParticipantToEvent(EXISTING_EVENT_ID, EXISTING_PARTICIPANT_ID);

        // Verify interactions and participant state
        Mockito.verify(participantRepository).findById(EXISTING_PARTICIPANT_ID);
        Mockito.verify(eventRepository).findById(EXISTING_EVENT_ID);
        Mockito.verify(participantRepository).save(expectedParticipant);
        Mockito.verify(eventRepository).save(expectedEvent);

        // Assert participant and event no longer reference each other
        assertThat(expectedParticipant.getEvents()).doesNotContain(expectedEvent);
        assertThat(expectedEvent.getParticipants()).doesNotContain(expectedParticipant);
    }

    @Test
    public void testCancelParticipantFromNonExistingEvent() throws Exception {
        // Mock data (event not found)
        Mockito.when(eventRepository.findById(EXISTING_EVENT_ID)).thenReturn(Optional.empty());
        Mockito.when(participantRepository.findById(EXISTING_PARTICIPANT_ID)).thenReturn(Optional.of(new Participant()));

        // Call the method (no changes expected)
        inscriptionService.cancelParticipantToEvent(EXISTING_EVENT_ID, EXISTING_PARTICIPANT_ID);

        // Verify no interaction with participant repository save
        Mockito.verify(participantRepository, times(0)).save(any(Participant.class));
    }

    @Test
    public void testCancelParticipantFromNonExistingParticipant() throws Exception {
        // Mock data (participant not found)
        Mockito.when(eventRepository.findById(EXISTING_EVENT_ID)).thenReturn(Optional.of(new Event()));
        Mockito.when(participantRepository.findById(EXISTING_PARTICIPANT_ID)).thenReturn(Optional.empty());

        // Call the method (no changes expected)
        inscriptionService.cancelParticipantToEvent(EXISTING_EVENT_ID, EXISTING_PARTICIPANT_ID);

        // Verify no interaction with event repository save
        Mockito.verify(eventRepository, times(0)).save(any(Event.class));
    }

    @Test
    public void testCancelParticipantNotRegistered() throws Exception {
        // Mock data (participant not registered to the event)
        Event expectedEvent = new Event();
        expectedEvent.setId(EXISTING_EVENT_ID);
        Participant expectedParticipant = new Participant();
        expectedParticipant.setId(EXISTING_PARTICIPANT_ID);

        Mockito.when(eventRepository.findById(EXISTING_EVENT_ID)).thenReturn(Optional.of(expectedEvent));
        Mockito.when(participantRepository.findById(EXISTING_PARTICIPANT_ID)).thenReturn(Optional.of(expectedParticipant));

        // Call the method (no changes expected)
        inscriptionService.cancelParticipantToEvent(EXISTING_EVENT_ID, EXISTING_PARTICIPANT_ID);

        // Verify no interaction with save methods
        Mockito.verify(participantRepository).save(any(Participant.class));
        Mockito.verify(eventRepository).save(any(Event.class));
    }


    @Test
    public void testGetAllParticipantsForExistingEvent() throws Exception {
        // Mock data
        Event expectedEvent = new Event();
        expectedEvent.setId(EXISTING_EVENT_ID);
        List<Participant> expectedParticipants = Arrays.asList(new Participant(), new Participant());
        expectedEvent.setParticipants(expectedParticipants);

        // Mocked behavior
        Mockito.when(eventRepository.findById(EXISTING_EVENT_ID)).thenReturn(Optional.of(expectedEvent));

        // Call the method
        List<Participant> participants = inscriptionService.getAllParticipantByEvents(EXISTING_EVENT_ID);

        // Verify interactions and returned list
        Mockito.verify(eventRepository).findById(EXISTING_EVENT_ID);
        assertThat(participants).containsExactlyElementsOf(expectedParticipants);
    }

    @Test
    public void testGetAllParticipantsForNonExistingEvent() throws Exception {
        // Mock data (event not found)
        Mockito.when(eventRepository.findById(EXISTING_EVENT_ID)).thenReturn(Optional.empty());

        // Call the method (expect empty list)
        List<Participant> participants = inscriptionService.getAllParticipantByEvents(EXISTING_EVENT_ID);

        // Verify interaction and returned empty list
        Mockito.verify(eventRepository).findById(EXISTING_EVENT_ID);
        assertThat(participants).isEmpty();
    }
}