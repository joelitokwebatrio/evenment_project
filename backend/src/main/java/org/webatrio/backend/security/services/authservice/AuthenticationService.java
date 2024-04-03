package org.webatrio.backend.security.services.authservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.webatrio.backend.events.exceptions.ParticipantNotFoundException;
import org.webatrio.backend.security.config.JwtService;
import org.webatrio.backend.security.dao.ParticipantRepository;
import org.webatrio.backend.security.dao.TokenRepository;
import org.webatrio.backend.security.dto.AuthenticationRequest;
import org.webatrio.backend.security.dto.AuthenticationResponse;
import org.webatrio.backend.security.dto.ChangePasswordRequest;
import org.webatrio.backend.security.dto.RegisterRequest;
import org.webatrio.backend.security.enums.TokenType;
import org.webatrio.backend.security.exceptions.PasswordException;
import org.webatrio.backend.security.models.participant.Participant;
import org.webatrio.backend.security.models.token.Token;

import java.io.IOException;
import java.security.Principal;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.webatrio.backend.events.utils.Utils.PARTICIPANT_NOT_FOUND;
import static org.webatrio.backend.security.utils.Utils.PASSWORD_ARE_NOT_THE_SAME;
import static org.webatrio.backend.security.utils.Utils.WRONG_PASSWORD;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final ParticipantRepository participantRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * Enregistrer un nouveau participant dans notre base de donnees
     *
     * @param request
     * @return
     */
    public AuthenticationResponse register(RegisterRequest request) {
        var user = Participant.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .username(request.getUsername())
                .gender(request.getGender())
                .build();

        var savedUser = participantRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(savedUser, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    /**
     * Authentifier un nouvelle utilisateur dans notre base de donnees
     *
     * @param request
     * @return
     */
    public AuthenticationResponse authenticate(AuthenticationRequest request) throws ParticipantNotFoundException {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user = participantRepository.findParticipantByEmail(request.getEmail())
                .orElseThrow(() -> new ParticipantNotFoundException(PARTICIPANT_NOT_FOUND));
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    /**
     * enregistrement du token dans notre base de donnees
     *
     * @param user
     * @param jwtToken
     */
    private void saveUserToken(Participant user, String jwtToken) {
        var token = Token.builder()
                .participant(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    /**
     * recuperation du token valide en fonction de l'utilisateur
     *
     * @param user
     */
    private void revokeAllUserTokens(Participant user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    /**
     * rafrechir le token de l'utilisateur present dans notre base de donnees
     *
     * @param request
     * @param response
     * @throws IOException
     */
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            var user = this.participantRepository.findParticipantByEmail(userEmail).orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }


    public void changePassword(ChangePasswordRequest request, Principal connectedUser) throws PasswordException {

        var user = (Participant) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        /**
         *  check if the current password is correct
         */
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new PasswordException(WRONG_PASSWORD);
        }
        /**
         * check if the two new passwords are the same
         */
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new PasswordException(PASSWORD_ARE_NOT_THE_SAME);
        }

        /**
         * update the password
         */
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        /**
         * save the new password
         */
        participantRepository.save(user);
    }

    /**
     * find Participant by USERNAME
     *
     * @param email
     * @return
     * @throws ParticipantNotFoundException
     */
    public Participant findParticipantByEmail(String email) throws ParticipantNotFoundException {
        Participant participant = participantRepository.findParticipantByEmail(email).orElse(null);
        if (Objects.isNull(participant)) {
            throw new ParticipantNotFoundException(String.format("This email %s do not exist", email));
        }
        return participant;
    }

    /**
     * Check if  username is available
     *
     * @param username
     * @return
     */
    public boolean isUsernameAvailable(String username) {
        Participant participant = participantRepository.findParticipantByFirstname(username).orElse(null);
        return participant == null;
    }

    /**
     * Check if email is available
     *
     * @param email
     * @return
     */

    public boolean isEmailAvailable(String email) {
        Participant participant = participantRepository.findParticipantByEmail(email).orElse(null);
        return participant == null;
    }

    public List<Participant> getAllParticipants() {
        return participantRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Participant::getId).reversed())
                .toList();
    }

    public void deleteParticipant(int id) {
        participantRepository.deleteById(id);
    }

    public Participant getParticipantById(int id) throws ParticipantNotFoundException {
        return participantRepository.findById(id)
                .orElseThrow(() -> new ParticipantNotFoundException(PARTICIPANT_NOT_FOUND));
    }

    /**
     * A modifier cette methode pour ameliorer la modificaton des informations dans la base de donnees
     *
     * @param participant
     * @return
     * @throws ParticipantNotFoundException
     */
    public Participant updateParticipant(Participant participant) throws ParticipantNotFoundException {
        if (Objects.nonNull(participant.getEmail())
                && Objects.nonNull(participant.getFirstname())
                && Objects.nonNull(participant.getLastname())) {
            Optional<Participant> participantOptional = participantRepository.findById(participant.getId());
            if (participantOptional.isEmpty()) {
                throw new ParticipantNotFoundException(PARTICIPANT_NOT_FOUND);
            }
            mapParticipant(participant, participantOptional);
            return participantRepository.save(participant);
        }
        return null;
    }

    private void mapParticipant(Participant participant, Optional<Participant> participantOptional) {

        if (participantOptional.isPresent()) {
            participantOptional.get().setEmail(participant.getEmail());
            participantOptional.get().setFirstname(participant.getFirstname());
            participantOptional.get().setLastname(participant.getLastname());
            participantOptional.get().setRole(participant.getRole());
            participantOptional.get().setGender(participant.getGender());
            participantOptional.get().setUsername(participant.getUsername());
        }
    }

}
