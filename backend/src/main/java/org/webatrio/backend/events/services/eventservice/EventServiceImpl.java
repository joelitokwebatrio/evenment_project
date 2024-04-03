package org.webatrio.backend.events.services.eventservice;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.webatrio.backend.events.dao.EventRepository;
import org.webatrio.backend.events.dto.EventDTO;
import org.webatrio.backend.events.exceptions.EventAlreadyExistException;
import org.webatrio.backend.events.exceptions.EventErrorException;
import org.webatrio.backend.events.exceptions.EventNotFoundException;
import org.webatrio.backend.events.mappers.EventsMapper;
import org.webatrio.backend.events.models.Event;
import org.webatrio.backend.security.dao.ParticipantRepository;
import org.webatrio.backend.security.models.participant.Participant;

import java.util.*;

import static org.webatrio.backend.events.utils.Utils.*;

@Service
@RequiredArgsConstructor
@Transactional
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final ParticipantRepository participantRepository;
    private final EventsMapper eventsMapper;


    /**
     * Ajouter un evenement a dans la base de donnees
     *
     * @param eventDTO
     * @return
     * @throws EventAlreadyExistException
     */
    @Override
    public EventDTO addEvent(EventDTO eventDTO) throws EventAlreadyExistException {
        if (Objects.nonNull(eventDTO) && Objects.nonNull(eventDTO.getTitle())) {
            Optional<Event> eventOptional = eventRepository.findEventByTitle(eventDTO.getTitle());
            if (eventOptional.isPresent()) {
                throw new EventAlreadyExistException(EVENT_ALREADY_EXIST_IN_DATABASE);
            }
        }
        Event event = eventRepository.save(eventsMapper.mapEventDTOToEvent(eventDTO));

        return eventsMapper.mapEventToEventDTO(event);
    }


    @Override
    public Optional<EventDTO> getEvent(Long idEvent) {
        return eventRepository.findById(idEvent)
                .map(eventsMapper::mapEventToEventDTO);
    }

    @Override
    public void deleteEvent(Long idEvent) {
        eventRepository.deleteById(idEvent);
    }

    /**
     * recuperer l'ensemble des evenements qui sont present dans la base de donnees
     */
    @Override
    public List<EventDTO> getEvents(String location) {
        return getEventDTOSByLocationAndPage(location);


    }

    /**
     * recuperation de tous les evenements en fonction des place
     */
    private List<EventDTO> getEventDTOSByLocationAndPage(String location) {
        return eventRepository.findAll().stream()
                .filter(event -> event.getPlace().contains(location))
                .sorted(Comparator.comparing(Event::getId).reversed())
                .map(eventsMapper::mapEventToEventDTO)

                .toList();

    }

    /**
     * Modification des evenements de notre base de donnees
     *
     * @param eventDTO
     * @return
     * @throws EventNotFoundException
     * @throws EventErrorException
     */
    @Override
    public EventDTO updateEvent(EventDTO eventDTO) throws EventNotFoundException, EventErrorException {
        if (Objects.nonNull(eventDTO) && Objects.nonNull(eventDTO.getTitle())) {

            Optional<Event> eventOptional = eventRepository.findById(eventDTO.getId());
            if (eventOptional.isEmpty()) {
                throw new EventNotFoundException(EVENT_NOT_EXIST);
            }

            setEventValues(eventDTO, eventOptional);

            return eventsMapper.mapEventToEventDTO(eventRepository.save(eventOptional.get()));
        }
        throw new EventErrorException(ERROR_UNKNOWM);
    }

    /**
     * Cette methode doit me permettre de retourer l'ensemble des evenements d'un utilisateur en fonction de son id
     *
     * @param id
     * @return
     */
    @Override
    public List<EventDTO> getEventByIdParticipant(Integer id) {
        List<Event> events = participantRepository.findById(id)
                .map(Participant::getEvents).orElse(Collections.emptyList());
        return events.stream().map(eventsMapper::mapEventToEventDTO).toList();

    }


    private void setEventValues(EventDTO eventDTO, Optional<Event> eventOptional) {
        if (eventOptional.isPresent()) {
            eventOptional.get().setTitle(eventsMapper.mapEventDTOToEvent(eventDTO).getTitle());
            eventOptional.get().setDescription(eventsMapper.mapEventDTOToEvent(eventDTO).getDescription());
            eventOptional.get().setStartEventDate(eventsMapper.mapEventDTOToEvent(eventDTO).getStartEventDate());
            eventOptional.get().setEndEventDate(eventsMapper.mapEventDTOToEvent(eventDTO).getEndEventDate());
            eventOptional.get().setPlace(eventsMapper.mapEventDTOToEvent(eventDTO).getPlace());
        }
    }

}
