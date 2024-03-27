package org.webatrio.backend.events.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.webatrio.backend.events.models.Event;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findEventByTitle(String title);
    List<Event> findEventByPlaceContaining(String keyword);
}
