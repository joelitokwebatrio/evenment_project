package org.webatrio.backend.events.dto;

import lombok.*;

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
    private List<String> listEmailsParticipant = new ArrayList<>();
    private boolean status;
    private long totalOfThisTypeElement;

}
