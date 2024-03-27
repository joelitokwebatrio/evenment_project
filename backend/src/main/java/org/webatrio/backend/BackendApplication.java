package org.webatrio.backend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.webatrio.backend.events.dao.EventRepository;
import org.webatrio.backend.events.dto.EventDTO;
import org.webatrio.backend.events.services.eventservice.EventService;
import org.webatrio.backend.security.dao.ParticipantRepository;
import org.webatrio.backend.security.dto.RegisterRequest;
import org.webatrio.backend.security.enums.Gender;
import org.webatrio.backend.security.services.authservice.AuthenticationService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.webatrio.backend.security.enums.Role.ORGANIZER;
import static org.webatrio.backend.security.enums.Role.PARTICIPANT;

@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(
            AuthenticationService service,
            EventService eventService,
            EventRepository eventRepository,
            ParticipantRepository participantRepository) {
        return args -> {
            eventRepository.deleteAll();
            eventService.addEvent(EventDTO.builder()
                    .title("rama")
                    .description("pour la fete pour la fete de ramadam")
                    .startEventDate(LocalDateTime.of(LocalDate.of(1999, 2, 2), LocalTime.of(10, 10)))
                    .endEventDate(LocalDateTime.of(LocalDate.of(2000, 2, 2), LocalTime.of(11, 11)))
                    .place("paris")
                    .numberOfParticipants(200)
                    .status(true)
                    .organiserName("felix")
                    .build());

            eventService.getEvents(1, 1, "paris").stream().map(EventDTO::getPlace).forEach(System.out::println);

            eventService.addEvent(EventDTO.builder()
                    .title("ramadam")
                    .description("pour la fete pour la fete de ramadam")
                    .startEventDate(LocalDateTime.of(LocalDate.of(1999, 2, 2), LocalTime.of(10, 10)))
                    .endEventDate(LocalDateTime.of(LocalDate.of(2000, 2, 2), LocalTime.of(11, 11)))
                    .place("quatar")
                    .numberOfParticipants(2000)
                    .status(true)
                    .organiserName("ethan")
                    .build());
            eventService.getEvents(1, 1, "quatar").stream().map(EventDTO::getPlace).forEach(System.out::println);

            participantRepository.deleteAll();
            var organizer = RegisterRequest.builder()
                    .firstname("Participant")
                    .lastname("Participant")
                    .email("joel@mail.com")
                    .password("1234")
                    .role(ORGANIZER)
                    .gender(Gender.MALE)
                    .username("jires")
                    .titleEvents(List.of("ramad"))
                    .build();
            System.out.println("Organizer token: " + service.register(organizer).getAccessToken());

            var participant = RegisterRequest.builder()
                    .firstname("Participant")
                    .lastname("Participant")
                    .email("jojo@mail.com")
                    .password("123456")
                    .role(PARTICIPANT)
                    .gender(Gender.MALE)
                    .username("giles")
                    .titleEvents(List.of("rama"))
                    .build();
            System.out.println("Participant token: " + service.register(participant).getAccessToken());
        };
    }

}
