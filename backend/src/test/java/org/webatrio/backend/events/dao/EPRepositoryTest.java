package org.webatrio.backend.events.dao;

import org.webatrio.backend.events.models.EP;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@ActiveProfiles("test")
class EPRepositoryTest {
    @Autowired
    public EPRepository epRepository;


    @BeforeEach
    void setUp() {
    }

    @Test
    void
    findEPByParticipantEmail200() {
        //given
        epRepository.save(getEP());

        //when
        EP ep = epRepository.findEPByParticipantEmail("joel@gmail.com").orElse(null);

        //then
        assertThat(ep).isNotNull();
    }

    @Test
    void findEPByParticipantEmail404() {
        //given
        //ne rien ajouter

        //when
        EP ep = epRepository.findEPByParticipantEmail("joel@gmail.com").orElse(null);

        //then
        assertThat(ep).isNull();


    }

    @Test
    void findEPByEventTitle200() {
        //given
        epRepository.save(getEP());

        //when
        List<EP> ep = epRepository.findEPByEventTitle("noel");

        //then
        assertThat(ep).isNotNull();


    }

    @Test
    void findEPByEventTitle404() {
        //given

        //when
        List<EP> ep = epRepository.findEPByEventTitle("noel");

        //then
        assertThat(ep).isEqualTo(Collections.emptyList());
    }


    private EP getEP() {
        return EP.builder().id(1L)
                .participantEmail("joel@gmail.com")
                .participantId(1)
                .eventId(1L)
                .eventTitle("noel")
                .build();
    }

}