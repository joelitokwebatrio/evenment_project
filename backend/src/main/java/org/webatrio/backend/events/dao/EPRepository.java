package org.webatrio.backend.events.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.webatrio.backend.events.models.EP;

import java.util.List;
import java.util.Optional;

@Repository
public interface EPRepository extends JpaRepository<EP, Long> {
    Optional<EP> findEPByParticipantEmail(String email);

    List<EP> findEPByEventTitle(String title);
}
