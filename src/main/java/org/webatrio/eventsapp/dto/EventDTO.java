package org.webatrio.eventsapp.dto;

import jakarta.persistence.*;
import lombok.*;
import org.webatrio.eventsapp.models.Participant;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventDTO {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime startEventDate;
    private LocalDateTime endEventDate;
    private String place;
    private Integer numberOfParticipants;
    private String organiserName;
    private boolean status;

    private List<ParticipantDTO> participantDTOS = new ArrayList<>();
}
