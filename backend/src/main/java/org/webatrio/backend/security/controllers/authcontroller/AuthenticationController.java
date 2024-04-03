package org.webatrio.backend.security.controllers.authcontroller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.webatrio.backend.events.exceptions.ParticipantNotFoundException;
import org.webatrio.backend.security.dto.AuthenticationRequest;
import org.webatrio.backend.security.dto.AuthenticationResponse;
import org.webatrio.backend.security.dto.ChangePasswordRequest;
import org.webatrio.backend.security.dto.RegisterRequest;
import org.webatrio.backend.security.exceptions.PasswordException;
import org.webatrio.backend.security.models.participant.Participant;
import org.webatrio.backend.security.services.authservice.AuthenticationService;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:4200")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(this.authenticationService.register(request));
    }

    @PostMapping("/authenticate")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) throws ParticipantNotFoundException {
        AuthenticationResponse idToken = this.authenticationService.authenticate(request);
        return ResponseEntity.ok(idToken);
    }

    @PostMapping("/refresh-token")
    @SecurityRequirement(name = "Bearer Authentication")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        this.authenticationService.refreshToken(request, response);
    }

    @PatchMapping("/change-password")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request, Principal connectedUser) throws PasswordException {
        this.authenticationService.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/profile")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Participant> getUserProfile(String email) throws ParticipantNotFoundException {
        return ResponseEntity.ok(this.authenticationService.findParticipantByEmail(email));
    }

    @GetMapping("/isUsernameAvailable")
    public boolean isUsernameAvailable(String username) {
        return authenticationService.isUsernameAvailable(username);
    }

    @GetMapping("/isEmailAvailable")
    public boolean isEmailAvailable(String email) {
        log.info(email);
        return authenticationService.isEmailAvailable(email);
    }

    @GetMapping("/participants")
    private List<Participant> getAllParticipants() {
        return authenticationService.getAllParticipants();
    }

    @DeleteMapping("/participant/delete/{id}")
    private ResponseEntity<Void> deleteParticipant(@PathVariable("id") Integer id) {
        authenticationService.deleteParticipant(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/participant/{id}")
    private ResponseEntity<Participant> getParticipantById(@PathVariable("id") Integer id) throws ParticipantNotFoundException {
        return ResponseEntity.ok(authenticationService.getParticipantById(id));
    }

    @PatchMapping("/participant/update")
    private ResponseEntity<Participant> updateParticipant(@RequestBody  Participant participant) throws ParticipantNotFoundException {
        return ResponseEntity.ok(authenticationService.updateParticipant(participant));
    }

}
