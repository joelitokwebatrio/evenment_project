package org.webatrio.eventsapp.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.webatrio.eventsapp.models.Event;
import org.webatrio.eventsapp.models.Participant;

import java.util.Optional;

@Repository
public interface EventRepository  extends JpaRepository<Event,Long> {
Optional<Event> findEventByTitle(String title);
}
