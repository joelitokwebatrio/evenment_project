package com.webatrio.atrio.events.models;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

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
    private Integer numberOfParticipants;
    /**
     * cette variable sera utiliser pour annuler un evenement
     */
    private boolean status;
    /**
     * cette variable peut-etre utiliser pour filtrer un evenement par nom
     */
    private String organiserName;


}
