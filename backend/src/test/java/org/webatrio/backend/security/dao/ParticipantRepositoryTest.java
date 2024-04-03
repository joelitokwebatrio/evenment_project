package org.webatrio.backend.security.dao;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.webatrio.backend.events.dao.EventRepository;
import org.webatrio.backend.events.models.Event;
import org.webatrio.backend.security.enums.Gender;
import org.webatrio.backend.security.enums.Role;
import org.webatrio.backend.security.models.participant.Participant;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@ActiveProfiles("test")
class ParticipantRepositoryTest {
    @Autowired
    private ParticipantRepository participantRepository;
    @Test
    void findParticipantByEmail() {

        //given
        participantRepository.save(getParticipant());

        //when
       Optional<Participant> participant = participantRepository.findParticipantByEmail("joel@gmail.com");

        //then
        Assertions.assertThat(participant).isPresent();

    }

    @Test
    void findParticipantByEmailNotFound() {

        //given
        //when
        Optional<Participant> participant = participantRepository.findParticipantByEmail("joel@gmail.com");

        //then
        Assertions.assertThat(participant).isEmpty();

    }

    @Test
    void findParticipantByFirstname() {
        //given
        participantRepository.save(getParticipant());

        //when
        Optional<Participant> participant = participantRepository.findParticipantByFirstname("Tchoufa");

        //then
        Assertions.assertThat(participant).isPresent();
    }

    @Test
    void findParticipantByFirstnameNotFound() {
        //given
        //when
        Optional<Participant> participant = participantRepository.findParticipantByFirstname("Tchoufa");

        //then
        Assertions.assertThat(participant).isEmpty();
    }



    @Test
    void findParticipantByUsername() {
        //given
        participantRepository.save(getParticipant());

        //when
        Optional<Participant> participant = participantRepository.findParticipantByUsername("joel");

        //then
        Assertions.assertThat(participant).isPresent();
    }

    @Test
    void findParticipantByUsernameNotFound() {
        //given
        //when
        Optional<Participant> participant = participantRepository.findParticipantByUsername("joel");

        //then
        Assertions.assertThat(participant).isEmpty();
    }





   private  Participant getParticipant(){
        return  Participant.builder()
                .username("joel")
                .email("joel@gmail.com")
                .firstname("Tchoufa")
                .lastname("Nkouatchet")
                .role(Role.PARTICIPANT)
                .events(Collections.emptyList())
                .gender(Gender.MALE)
                .build();
    }

}