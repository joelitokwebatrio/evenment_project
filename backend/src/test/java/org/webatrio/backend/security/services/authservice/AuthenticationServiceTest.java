package org.webatrio.backend.security.services.authservice;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.webatrio.backend.security.dao.ParticipantRepository;
import org.webatrio.backend.security.enums.Role;
import org.webatrio.backend.security.models.participant.Participant;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AuthenticationServiceTest {

    @Mock
    private ParticipantRepository participantRepository;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    void register() {
        //given

        //when

        //then
    }

    @Test
    void authenticate() {
        //given

        //when

        //then
    }

    @Test
    void refreshToken() {
        //given

        //when

        //then
    }

    @Test
    void changePassword() {
        //given

        //when

        //then
    }

    @Test
    void findParticipantByEmail() {
        //given

        //when

        //then
    }

    @Test
    void isUsernameAvailable() {
        //given
        Mockito.when(participantRepository.findParticipantByUsername("joel")).thenReturn(getParticipant());
        //when
        boolean checkExistingParticipant = authenticationService.isUsernameAvailable("joel");

        //then
        Assertions.assertThat(checkExistingParticipant).isTrue();
    }

    @Test
    void isUsernameNotAvailable() {
        //given
        Mockito.when(participantRepository.findParticipantByUsername("joel")).thenReturn(Optional.empty());
        //when
        boolean checkExistingParticipant = authenticationService.isUsernameAvailable(null);

        //then
        Assertions.assertThat(checkExistingParticipant).isTrue();
    }

    @Test
    void isEmailAvailable() {
        //given

        //when

        //then
    }

    @Test
    void getAllParticipants() {
        //given

        //when

        //then
    }

    @Test
    void deleteParticipant() {
        //given

        //when

        //then
    }

    @Test
    void getParticipantById() {
        //given

        //when

        //then
    }

    @Test
    void updateParticipant() {
        //given

        //when

        //then
    }

    Optional<Participant> getParticipant() {
        var participant = Participant.builder()
                .id(1)
                .username("joel")
                .lastname("tchoufa")
                .firstname("tchoufa")
                .email("joel@gmail.com")
                .role(Role.PARTICIPANT)
                .password("1234")
                .build();
        return Optional.of(participant);
    }


}