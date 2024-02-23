package com.webatrio.atrio.events.dao;

import com.webatrio.atrio.events.models.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findEventByTitle(String title);
}
