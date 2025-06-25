package gr.aueb.cf.spot_a_bird_app.dto;

import gr.aueb.cf.spot_a_bird_app.core.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserInsertDTO {
    @NotEmpty(message = "Username in required")
    private String username;
    private String password;
    @Email(message="Invalid email")
    private String email;
    @NotEmpty(message = "firstname in required")
    private String firstname;
    @NotEmpty(message = "lastname in required")
    private String lastname;
    @NotNull(message = "role is required")
    private Role role;
    @NotNull(message = "isActive field is required")
    private Boolean isActive;
    @NotNull(message = "Profile details are required")
    private ProfileDetailsInsertDTO profileDetailsInsertDTO;

    public String getFullName() {
        return (firstname != null ? firstname : "") +
                (lastname != null ? " " + lastname : "");
    }
}
