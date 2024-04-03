package org.webatrio.backend.events.models;

import jakarta.persistence.*;
import lombok.*;
import org.webatrio.backend.security.models.participant.Participant;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private LocalDateTime startEventDate;
    private LocalDateTime endEventDate;
    private String place;
    @ManyToMany
    @JoinTable(name = "participant_event",
            joinColumns = @JoinColumn(name = "participant_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id"))
    private List<Participant> participants = new ArrayList<>();


}
