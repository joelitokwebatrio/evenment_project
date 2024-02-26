package com.webatrio.atrio.events.services.inscrptionservice;

import com.webatrio.atrio.events.dao.EPRepository;
import com.webatrio.atrio.events.dao.EventRepository;
import com.webatrio.atrio.events.exceptions.EventErrorException;
import com.webatrio.atrio.events.exceptions.EventNotFoundException;
import com.webatrio.atrio.events.exceptions.ParticipantNotFoundException;
import com.webatrio.atrio.events.models.EP;
import com.webatrio.atrio.events.models.Event;
import com.webatrio.atrio.security.dao.ParticipantRepository;
import com.webatrio.atrio.security.models.participant.Participant;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.webatrio.atrio.events.utils.Utils.*;

@Service
@RequiredArgsConstructor
@Transactional
public class InscriptionServiceImpl implements InscriptionService {
    private final EventRepository eventRepository;
    private final EPRepository epRepository;
    private final ParticipantRepository participantRepository;

    /**
     * Inscrire un participant a un evenement
     *
     * @param eventTitle
     * @param email
     * @throws EventNotFoundException
     * @throws ParticipantNotFoundException
     * @throws EventErrorException
     */
    @Override
    public void addParticipantToEvent(String eventTitle, String email) throws EventNotFoundException, ParticipantNotFoundException, EventErrorException {
        Optional<Event> event = eventRepository.findEventByTitle(eventTitle);
        Optional<Participant> participant = participantRepository.findByEmail(email);
        if (participant.isEmpty()) {
            throw new ParticipantNotFoundException(PARTICIPANT_NOT_EXIST);
        }
        if (event.isEmpty()) {
            throw new EventNotFoundException(EVENT_NOT_EXIST);
        }
        /**
         * Ajouter le nombre de participant a un evenement
         */
        int numberOfParticipant = event.get().getNumberOfParticipants();
        event.get().setNumberOfParticipants(numberOfParticipant + 1);
        eventRepository.save(event.get());
        /**
         * Ajouter les participant et les evenement dans la table de jointure
         * que j'utilise pour faire la correspondance
         */
        EP ep = new EP();
        ep.setEventId(event.get().getId());
        ep.setParticipantId(participant.get().getId());
        ep.setEventTitle(event.get().getTitle());
        ep.setParticipantEmail(participant.get().getEmail());

        if (epRepository.findEPByParticipantEmail(ep.getParticipantEmail())
                .stream().anyMatch(ep1 -> ep.getEventTitle().equals(ep1.getEventTitle()))) {
            throw new EventErrorException(ERROR_UNKNOWM);
        }
        epRepository.save(ep);
    }

        /**
         * Annuler un evenement
         * @param emailParticipant
         * @param titleEvent
         */

    @Override
    public void cancelParticipantToEvent(String emailParticipant, String titleEvent) {
        Optional<EP> epFind = epRepository.findEPByParticipantEmail(emailParticipant);
        epFind.ifPresent(epRepository::delete);
    }

    /**
     * Recuperation de tous les participant inscrire a un evenement.
     * @param titleEvent
     * @return
     */
    @Override
    public List<Participant> getAllParticipantByEvents(String titleEvent) {
        List<Participant> users = new ArrayList<>();
        List<EP> eps = epRepository.findEPByEventTitle(titleEvent);
        eps.stream().map(EP::getParticipantEmail).forEach(epEmail -> {
            Optional<Participant> userOptional = participantRepository.findByEmail(epEmail);
            userOptional.ifPresent(users::add);
        });

        return users;
    }
}
