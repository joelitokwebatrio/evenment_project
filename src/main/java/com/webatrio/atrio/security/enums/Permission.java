package com.webatrio.atrio.security.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {

    ORGANIZER_READ("organizer:read"),
    ORGANIZER_UPDATE("organizer:update"),
    ORGANIZER_CREATE("organizer:create"),
    ORGANIZER_DELETE("organizer:delete"),
    PARTICIPANT_READ("participant:read"),
    PARTICIPANT_UPDATE("participant:update"),
    PARTICIPANT_CREATE("participant:create"),
    PARTICIPANT_DELETE("participant:delete");

    @Getter
    private final String permission;
}
