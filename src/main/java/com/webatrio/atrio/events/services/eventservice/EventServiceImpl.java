package com.webatrio.atrio.events.services.eventservice;

import com.webatrio.atrio.events.dao.EventRepository;
import com.webatrio.atrio.events.dto.EventDTO;
import com.webatrio.atrio.events.exceptions.EventAlreadyExistException;
import com.webatrio.atrio.events.exceptions.EventErrorException;
import com.webatrio.atrio.events.exceptions.EventNotFoundException;
import com.webatrio.atrio.events.mappers.EventsMapper;
import com.webatrio.atrio.events.models.Event;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.webatrio.atrio.events.utils.Utils.*;

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
            if (eventOptional.isPresent()) {
                throw new EventAlreadyExistException(EVENT_ALREADY_EXIST_IN_DATABASE);
            }
        }
        Event event = eventRepository.save(eventsMapper.mapEventDTOToEvent(eventDTO));

        return eventsMapper.mapEventToEventDTO(event);
    }

    @Override
    public List<EventDTO> getEvents(int pageNo, int pageSize, String location) {
        List<EventDTO> getPageByLocation = getEventDTOSByLocationAndPage(pageNo, pageSize, location);
        if (getPageByLocation != null) return getPageByLocation;

        List<EventDTO> getPageWithoutLocation = getEventDTOSNotByLocation(pageNo, pageSize, location);
        if (getPageWithoutLocation != null) return getPageWithoutLocation;

        return Collections.emptyList();

    }

    private List<EventDTO> getEventDTOSByLocationAndPage(int pageNo, int pageSize, String location) {
        if (Strings.isNotBlank(location)) {
            Pageable pageable = PageRequest.of(pageNo, pageSize);
            Page<Event> page = eventRepository.findAll(pageable);
            if (Strings.isNotBlank(String.valueOf(pageNo))  && Strings.isNotBlank(String.valueOf(pageSize))) {
                return page.getContent().stream()
                        .filter(event -> event.getPlace().equalsIgnoreCase(location)).filter(Event::isStatus)
                        .map(eventsMapper::mapEventToEventDTO).toList();
            }else {
                return eventRepository.findAll()
                        .stream().filter(event -> event.getPlace().equalsIgnoreCase(location))
                        .filter(Event::isStatus).map(eventsMapper::mapEventToEventDTO)
                        .toList();}
        }
        return null;
    }

    private List<EventDTO> getEventDTOSNotByLocation(int pageNo, int pageSize, String location) {
        if (Strings.isBlank(location)) {
            if (Strings.isNotBlank(String.valueOf(pageNo)) && Strings.isNotBlank(String.valueOf(pageSize))) {
                Pageable pageable = PageRequest.of(pageNo, pageSize);
                Page<Event> page = eventRepository.findAll(pageable);
                return page.getContent().stream()
                        .filter(Event::isStatus).map(eventsMapper::mapEventToEventDTO)
                        .toList();
            } else {
                return eventRepository.findAll().stream().filter(Event::isStatus)
                        .map(eventsMapper::mapEventToEventDTO)
                        .toList();}
        }
        return null;
    }

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
