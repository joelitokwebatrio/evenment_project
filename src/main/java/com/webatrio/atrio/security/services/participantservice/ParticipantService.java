package com.webatrio.atrio.security.services.participantservice;

import com.webatrio.atrio.security.dao.ParticipantRepository;
import com.webatrio.atrio.security.dto.ChangePasswordRequest;
import com.webatrio.atrio.security.exceptions.PasswordException;
import com.webatrio.atrio.security.models.participant.Participant;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;

import static com.webatrio.atrio.security.utils.Utils.PASSWORD_ARE_NOT_THE_SAME;
import static com.webatrio.atrio.security.utils.Utils.WRONG_PASSWORD;

@Service
@RequiredArgsConstructor
public class ParticipantService {

    private final PasswordEncoder passwordEncoder;
    private final ParticipantRepository repository;

    /**
     * Changer le mots de passe d'un utilisateur present dans la base de donnees
     * @param request
     * @param connectedUser
     * @throws PasswordException
     */
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
        repository.save(user);
    }
}
