package org.webatrio.backend.security.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.webatrio.backend.security.models.participant.Participant;

import java.util.Optional;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Integer> {

    Optional<Participant> findParticipantByEmail(String email);
    Optional<Participant> findParticipantByFirstname(String firstname);
    Optional<Participant> findParticipantByUsername(String firstname);
}
