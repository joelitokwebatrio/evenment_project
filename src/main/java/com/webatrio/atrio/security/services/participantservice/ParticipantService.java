package com.webatrio.atrio.security.services.participantservice;

import com.webatrio.atrio.security.dao.ParticipantRepository;
import com.webatrio.atrio.security.dto.ChangePasswordRequest;
import com.webatrio.atrio.security.models.participant.Participant;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class ParticipantService {

    private final PasswordEncoder passwordEncoder;
    private final ParticipantRepository repository;

    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {

        var user = (Participant) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        // check if the current password is correct
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }
        // check if the two new passwords are the same
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new IllegalStateException("Password are not the same");
        }

        // update the password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        // save the new password
        repository.save(user);
    }
}
