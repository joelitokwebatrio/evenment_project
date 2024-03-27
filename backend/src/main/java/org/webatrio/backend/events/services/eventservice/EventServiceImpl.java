package org.webatrio.backend.events.services.eventservice;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.webatrio.backend.events.dao.EventRepository;
import org.webatrio.backend.events.dto.EventDTO;
import org.webatrio.backend.events.exceptions.EventAlreadyExistException;
import org.webatrio.backend.events.exceptions.EventErrorException;
import org.webatrio.backend.events.exceptions.EventNotFoundException;
import org.webatrio.backend.events.mappers.EventsMapper;
import org.webatrio.backend.events.models.Event;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.webatrio.backend.events.utils.Utils.*;

@Service
@RequiredArgsConstructor
@Transactional
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
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
     *
     * @param pageNo
     * @param pageSize
     * @param location
     * @return
     */
    @Override
    public List<EventDTO> getEvents(int pageNo, int pageSize, String location) {
        return getEventDTOSByLocationAndPage(pageNo, pageSize, location);


    }

    /**
     * recuperation de tous les evenements en fonction des place
     *
     * @param pageNo
     * @param pageSize
     * @param location
     * @return
     */
    private List<EventDTO> getEventDTOSByLocationAndPage(int pageNo, int pageSize, String location) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Event> page = eventRepository.findAll(pageable);
        long totalElements = page.getTotalElements();
        if (Strings.isNotBlank(String.valueOf(pageNo)) && Strings.isNotBlank(String.valueOf(pageSize))) {
            List<EventDTO> eventsDTO = page.getContent().stream()
                    .filter(event -> event.getPlace().contains(location))
                    .filter(Event::isStatus)
                    .map(eventsMapper::mapEventToEventDTO).toList();
            return addTotalElementsInfo(eventsDTO, totalElements);
        } else {
            List<EventDTO> eventsDTO = eventRepository.findAll().stream()
                    .filter(Event::isStatus)
                    .map(eventsMapper::mapEventToEventDTO)
                    .toList();
            return addTotalElementsInfo(eventsDTO, totalElements);
        }

    }

    private List<EventDTO> addTotalElementsInfo(List<EventDTO> eventsDTO, long totalElements) {
        eventsDTO.stream().findFirst()
                .ifPresent(eventDTO -> eventDTO.setTotalOfThisTypeElement(totalElements));
        return eventsDTO;
    }


    /**
     * changer l'etat des evenements de notre base de donnees.
     * En effet cela doit nous permet de rentre certain evenement invisible.
     * Ceci est fait pour ne pas supprimer les evenement de la base de donnees mais plustot les rentre invisible
     *
     * @param title
     * @return
     * @throws EventNotFoundException
     */
    @Override
    public EventDTO cancelEvent(String title) throws EventNotFoundException {
        Optional<Event> eventOptional = eventRepository.findAll()
                .stream().filter(event -> event.getTitle().equals(title))
                .findFirst();

        if (eventOptional.isEmpty()) {
            throw new EventNotFoundException(EVENT_NOT_EXIST);
        }

        eventOptional.ifPresent(event -> {
            event.setStatus(false);
            eventRepository.save(eventOptional.get());
        });


        return eventsMapper.mapEventToEventDTO(eventOptional.get());

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
            Optional<Event> eventOptional = eventRepository.findEventByTitle(eventDTO.getTitle());
            if (eventOptional.isEmpty()) {
                throw new EventNotFoundException(EVENT_NOT_EXIST);
            }

            setEventValues(eventDTO, eventOptional);

            return eventsMapper.mapEventToEventDTO(eventRepository.save(eventOptional.get()));
        }
        throw new EventErrorException(ERROR_UNKNOWM);
    }


    private void setEventValues(EventDTO eventDTO, Optional<Event> eventOptional) {
        if (eventOptional.isPresent()) {
            eventOptional.get().setTitle(eventsMapper.mapEventDTOToEvent(eventDTO).getTitle());
            eventOptional.get().setDescription(eventsMapper.mapEventDTOToEvent(eventDTO).getDescription());
            eventOptional.get().setStartEventDate(eventsMapper.mapEventDTOToEvent(eventDTO).getStartEventDate());
            eventOptional.get().setEndEventDate(eventsMapper.mapEventDTOToEvent(eventDTO).getEndEventDate());
            eventOptional.get().setPlace(eventsMapper.mapEventDTOToEvent(eventDTO).getPlace());
            eventOptional.get().setNumberOfParticipants(eventsMapper.mapEventDTOToEvent(eventDTO).getNumberOfParticipants());
            eventOptional.get().setStatus(eventsMapper.mapEventDTOToEvent(eventDTO).isStatus());
            eventOptional.get().setOrganiserName(eventsMapper.mapEventDTOToEvent(eventDTO).getOrganiserName());
        }

    }
}
