package org.webatrio.eventsapp.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.webatrio.eventsapp.enums.Role;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String surname;
    private String email;

//    @Enumerated(EnumType.STRING)
//    private Role role;

    @ManyToMany(mappedBy = "participants", fetch = FetchType.EAGER)
    private List<Event> events = new ArrayList<>();
}
