package org.webatrio.eventsapp.services.eventservice;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.webatrio.eventsapp.dao.EventRepository;
import org.webatrio.eventsapp.dto.EventDTO;
import org.webatrio.eventsapp.exceptions.EventAlreadyExistException;
import org.webatrio.eventsapp.exceptions.EventErrorException;
import org.webatrio.eventsapp.exceptions.EventNotFoundException;
import org.webatrio.eventsapp.mappers.EventsMapper;
import org.webatrio.eventsapp.models.Event;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.webatrio.eventsapp.utils.Utils.*;

@Service
@RequiredArgsConstructor
@Transactional
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final EventsMapper eventsMapper;


    @Override
    public EventDTO addEvent(EventDTO eventDTO) throws EventAlreadyExistException {
        if (Objects.nonNull(eventDTO) && Objects.nonNull(eventDTO.getTitle())) {
            Optional<Event> eventOptional = eventRepository.findEventByTitle(eventDTO.getTitle());
            if (eventOptional.isPresent()){
                throw  new EventAlreadyExistException(EVENT_ALREADY_EXIST_IN_DATABASE);
            }
        }
        Event event = eventRepository.save(eventsMapper.mapEventDTOToEvent(eventDTO));

        return eventsMapper.mapEventToEventDTO(event);
    }

    @Override
    public List<EventDTO> getEvents(String location) {

        if (location!=null && location.isEmpty()){
            return eventRepository.findAll()
                    .stream()
                    .filter(event -> event.getPlace().equalsIgnoreCase(location))
                    .filter(Event::isStatus)
                    .map(eventsMapper::mapEventToEventDTO)
                    .toList();
        }else{
            return eventRepository.findAll()
                    .stream()
                    .filter(Event::isStatus)
                    .map(eventsMapper::mapEventToEventDTO)
                    .toList();
        }

    }

    @Override
    public EventDTO cancelEvent(String title) throws EventNotFoundException {
    Optional<Event> eventOptional = eventRepository.findAll()
            .stream()
            .filter(event -> event.getTitle().equals(title))
            .findFirst();
    if (eventOptional.isEmpty()){
        throw  new EventNotFoundException(EVENT_NOT_EXIST);
    }

        eventOptional.ifPresent(event -> {
            event.setStatus(false);
        eventRepository.save(eventOptional.get());
        });


    return eventsMapper.mapEventToEventDTO(eventOptional.get());

    }

    @Override
    public EventDTO updateEvent(EventDTO eventDTO) throws EventNotFoundException, EventErrorException {
        if (Objects.nonNull(eventDTO) && Objects.nonNull(eventDTO.getTitle())) {
            Optional<Event> eventOptional = eventRepository.findEventByTitle(eventDTO.getTitle());
            if (eventOptional.isEmpty()){
                throw  new EventNotFoundException(EVENT_NOT_EXIST);
            }

            setEventValues(eventDTO, eventOptional);

            return eventsMapper.mapEventToEventDTO(eventRepository.save(eventOptional.get()));
        }
        throw new EventErrorException(ERROR_UNKNOWM);
    }


    private void setEventValues(EventDTO eventDTO, Optional<Event> eventOptional) {
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
