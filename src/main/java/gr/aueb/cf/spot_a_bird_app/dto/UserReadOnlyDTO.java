package gr.aueb.cf.spot_a_bird_app.dto;

import gr.aueb.cf.spot_a_bird_app.core.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserReadOnlyDTO {
    private Long id;  // Include ID for reference
    private String username;
    private String email;
    private String firstname;
    private String lastname;
    private Role role;
    private ProfileDetailsReadOnlyDTO profileDetails;
}
