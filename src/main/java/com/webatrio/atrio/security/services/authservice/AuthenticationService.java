package com.webatrio.atrio.security.services.authservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.webatrio.atrio.events.exceptions.ParticipantNotFoundException;
import com.webatrio.atrio.security.config.JwtService;
import com.webatrio.atrio.security.dao.ParticipantRepository;
import com.webatrio.atrio.security.dao.TokenRepository;
import com.webatrio.atrio.security.dto.AuthenticationRequest;
import com.webatrio.atrio.security.dto.AuthenticationResponse;
import com.webatrio.atrio.security.dto.RegisterRequest;
import com.webatrio.atrio.security.enums.TokenType;
import com.webatrio.atrio.security.models.participant.Participant;
import com.webatrio.atrio.security.models.token.Token;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static com.webatrio.atrio.events.utils.Utils.PARTICIPANT_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final ParticipantRepository repository;
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
                .build();
        var savedUser = repository.save(user);
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
        var user = repository.findByEmail(request.getEmail()).orElseThrow(()->new ParticipantNotFoundException(PARTICIPANT_NOT_FOUND));
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
            var user = this.repository.findByEmail(userEmail).orElseThrow();
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
}
