package gr.aueb.cf.spot_a_bird_app.dto;

import gr.aueb.cf.spot_a_bird_app.core.enums.Role;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserReadOnlyDTO {
    private Long id;  // Include ID for reference
    private String username;
    private String email;
    private String firstname;
    private String lastname;
    private Role role;
    private Boolean isActive;
    private ProfileDetailsReadOnlyDTO profileDetails;
}
