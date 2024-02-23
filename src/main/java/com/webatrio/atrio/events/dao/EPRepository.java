package com.webatrio.atrio.events.dao;

import com.webatrio.atrio.events.models.EP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EPRepository extends JpaRepository<EP,Long> {
    Optional<EP>  findEPByParticipantEmail(String email);
    List<EP> findEPByEventTitle(String title);
}
