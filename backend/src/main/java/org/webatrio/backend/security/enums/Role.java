package org.webatrio.backend.security.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.webatrio.backend.security.enums.Permission.*;


@RequiredArgsConstructor
public enum Role {

    USER(Collections.emptySet()),
    ORGANIZER(
            Set.of(
                    ORGANIZER_READ,
                    ORGANIZER_UPDATE,
                    ORGANIZER_DELETE,
                    ORGANIZER_CREATE,
                    PARTICIPANT_READ,
                    PARTICIPANT_UPDATE,
                    PARTICIPANT_DELETE,
                    PARTICIPANT_CREATE
            )
    ),
    PARTICIPANT(
            Set.of(
                    PARTICIPANT_READ,
                    PARTICIPANT_UPDATE,
                    PARTICIPANT_DELETE,
                    PARTICIPANT_CREATE
            )
    );

    @Getter
    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}
