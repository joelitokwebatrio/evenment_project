package com.webatrio.atrio.security.controllers.participantcontroller;

import com.webatrio.atrio.security.dto.ChangePasswordRequest;
import com.webatrio.atrio.security.services.participantservice.ParticipantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class ParticipantController {
    private final ParticipantService participantService;

    @PatchMapping
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request, Principal connectedUser
    ) {
        participantService.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
    }
}
