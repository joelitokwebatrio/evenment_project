package com.webatrio.atrio.security.dao;

import com.webatrio.atrio.security.models.participant.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
@EnableJpaRepositories
public interface ParticipantRepository extends JpaRepository<Participant, Integer> {

    Optional<Participant> findByEmail(String email);

}
