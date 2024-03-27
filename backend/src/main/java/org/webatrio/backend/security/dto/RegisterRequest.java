package org.webatrio.backend.security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.webatrio.backend.security.enums.Gender;
import org.webatrio.backend.security.enums.Role;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private Gender gender;
    private Role role = Role.PARTICIPANT;
    List<String> titleEvents = new ArrayList<>();
}
