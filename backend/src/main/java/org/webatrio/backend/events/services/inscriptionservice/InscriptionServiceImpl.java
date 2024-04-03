package org.webatrio.backend.events.services.inscriptionservice;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.webatrio.backend.events.dao.EventRepository;
import org.webatrio.backend.events.exceptions.EventErrorException;
import org.webatrio.backend.events.exceptions.EventNotFoundException;
import org.webatrio.backend.events.exceptions.ParticipantNotFoundException;
import org.webatrio.backend.events.models.Event;
import org.webatrio.backend.security.dao.ParticipantRepository;
import org.webatrio.backend.security.models.participant.Participant;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.webatrio.backend.events.utils.Utils.EVENT_NOT_EXIST;
import static org.webatrio.backend.events.utils.Utils.PARTICIPANT_NOT_EXIST;


@Service
@RequiredArgsConstructor
@Transactional
public class InscriptionServiceImpl implements InscriptionService {
    private final EventRepository eventRepository;
    private final ParticipantRepository participantRepository;

    /**
     * Inscrire un participant a un evenement
     *
     * @param idEvent
     * @param idParticipant
     * @throws EventNotFoundException
     * @throws ParticipantNotFoundException
     * @throws EventErrorException
     */
    @Override
    public void addParticipantToEvent(Long idEvent, Integer idParticipant) throws EventNotFoundException, ParticipantNotFoundException, EventErrorException {
        Optional<Event> event = eventRepository.findById(idEvent);
        Optional<Participant> participant = participantRepository.findById(idParticipant);
        if (participant.isEmpty()) {
            throw new ParticipantNotFoundException(PARTICIPANT_NOT_EXIST);
        }
        if (event.isEmpty()) {
            throw new EventNotFoundException(EVENT_NOT_EXIST);
        }
        boolean noAlreadyRegister = participant.stream()
                .map(Participant::getEvents)
                .flatMap(Collection::stream).noneMatch(e -> event.get().equals(e));

        if (noAlreadyRegister) {
            participant.ifPresent(p -> p.getEvents().add(event.get()));
            event.ifPresent(e -> e.getParticipants().add(participant.get()));
        }

        /**
         * ajouter un participant a un evenement
         */

        participantRepository.save(participant.get());

    }

    /**
     * supprimer un participant d un evenement
     *
     * @param idEvent
     * @param idParticipant
     */

    @Override
    public void cancelParticipantToEvent(Long idEvent, Integer idParticipant) {
        Optional<Event> eventOptional = eventRepository.findById(idEvent);
        Optional<Participant> participantOptional = participantRepository.findById(idParticipant);
        if (participantOptional.isPresent() && eventOptional.isPresent()) {
            participantOptional.get().getEvents().remove(eventOptional.get());
            eventOptional.get().getParticipants().remove(participantOptional.get());
            eventRepository.save(eventOptional.get());
            participantRepository.save(participantOptional.get());
        }
    }

    /**
     * Recuperation de tous les participant inscrire a un evenement.
     *
     * @param idEvent
     * @return
     */
    @Override
    public List<Participant> getAllParticipantByEvents(Long idEvent) {
        return eventRepository.findById(idEvent)
                .map(Event::getParticipants)
                .stream().flatMap(Collection::stream).toList();

    }



}