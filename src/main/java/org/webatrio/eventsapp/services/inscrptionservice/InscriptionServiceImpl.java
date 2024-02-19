package org.webatrio.eventsapp.services.inscrptionservice;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.webatrio.eventsapp.dao.EventRepository;
import org.webatrio.eventsapp.dao.ParticipantRepository;
import org.webatrio.eventsapp.dto.ParticipantDTO;
import org.webatrio.eventsapp.exceptions.EventNotFoundException;
import org.webatrio.eventsapp.exceptions.ParticipantNotFoundException;
import org.webatrio.eventsapp.mappers.ParticipantMapper;
import org.webatrio.eventsapp.models.Event;
import org.webatrio.eventsapp.models.Participant;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.webatrio.eventsapp.utils.Utils.EVENT_NOT_EXIST;
import static org.webatrio.eventsapp.utils.Utils.PARTICIPANT_NOT_EXIST;

@Service
@RequiredArgsConstructor
@Transactional
public class InscriptionServiceImpl implements  InscriptionService{
    private final EventRepository eventRepository;
    private final ParticipantRepository participantRepository;
    private final ParticipantMapper participantMapper;
    @Override
    public void addParticipantToEvent(String eventTitle, String email) throws EventNotFoundException, ParticipantNotFoundException {
        Optional<Event> event = eventRepository.findEventByTitle(eventTitle);
        Optional<Participant> participant = participantRepository.findParticipantByEmail(email);

        if (participant.isEmpty()){
            throw new ParticipantNotFoundException(PARTICIPANT_NOT_EXIST);
        }
        if (event.isEmpty()){
            throw new EventNotFoundException(EVENT_NOT_EXIST);
        }
        event.ifPresent(e -> {e.getParticipants().add(participant.get());});

    }

    @Override
    public void cancelParticipantToEvent(String emailParticipant, String titleEvent){
        Optional<Participant> participant = participantRepository.findParticipantByEmail(emailParticipant);
        Optional<Event> event = eventRepository.findEventByTitle(titleEvent);
        participant.ifPresent(value -> {
            event.stream().map(Event::getParticipants).flatMap(Collection::stream).forEach(participantSave -> {
                if (participantSave.equals(value)) {
                    participantRepository.delete(participantSave);
                }
            });
        });
    }

    @Override
    public List<ParticipantDTO> getAllParticipantByEvents(String titleEvent){

        return eventRepository.findAll()
                .stream()
                .filter(event -> event.getTitle().equals(titleEvent))
                .map(Event::getParticipants)
                .flatMap(Collection::stream)
                .map(participantMapper::mapParticipantToParticipantDTO).toList();
    }
}
