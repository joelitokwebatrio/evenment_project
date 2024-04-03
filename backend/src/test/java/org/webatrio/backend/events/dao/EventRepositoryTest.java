package org.webatrio.backend.events.dao;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.webatrio.backend.events.models.Event;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@ActiveProfiles("test")
class EventRepositoryTest {

    @Autowired
    private EventRepository eventRepository;

    @BeforeEach
    void setUp() {
    }

    @Test
    void findEventByTitle200() {
        //given
        eventRepository.save(getEvent());

        //when
        Event ep = eventRepository.findEventByTitle("rama").orElse(null);

        //then
        Assertions.assertThat(ep).isNotNull();

    }

    @Test
    void findEventByTitleNotFound() {
        //given
        //when
        Event ep = eventRepository.findEventByTitle("rama").orElse(null);

        //then
        Assertions.assertThat(ep).isNull();

    }

    @Test
    void findEventByPlaceContaining() {
        //given
        eventRepository.save(getEvent());

        //when
        List<Event> events = eventRepository.findEventByPlaceContaining("p");

        //then
        Assertions.assertThat(events).isNotNull();
    }

    @Test
    void findEventByPlaceContainingNotFound() {
        //given

        //when
        List<Event> events = eventRepository.findEventByPlaceContaining("");

        //then
        Assertions.assertThat(events).isNotNull();
    }



    public Event getEvent() {
        return eventRepository.save(Event.builder()
                .title("rama")
                .description("pour la fete pour la fete de ramadam")
                .startEventDate(LocalDateTime.of(LocalDate.of(1999, 2, 2), LocalTime.of(10, 10)))
                .endEventDate(LocalDateTime.of(LocalDate.of(2000, 2, 2), LocalTime.of(11, 11)))
                .place("paris")
                .numberOfParticipants(200)
                .status(true)
                .organiserName("felix")
                .build());
    }

}