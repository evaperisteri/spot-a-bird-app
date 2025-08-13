package gr.aueb.cf.spot_a_bird_app.core.filters;

import gr.aueb.cf.spot_a_bird_app.core.enums.Gender;
import gr.aueb.cf.spot_a_bird_app.core.enums.Role;
import lombok.*;
import org.springframework.lang.Nullable;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserFilters extends GenericFilters {

    private Long id;
    private String username;
    private String email;
    private Role role;

    @Nullable
    private Boolean isActive;

    @Nullable
    private Gender gender;

//    @Nullable
//    private LocalDate dateOfBirth;


}
