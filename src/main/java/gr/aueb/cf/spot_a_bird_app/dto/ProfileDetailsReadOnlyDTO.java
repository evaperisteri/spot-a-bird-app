package gr.aueb.cf.spot_a_bird_app.dto;

import gr.aueb.cf.spot_a_bird_app.core.enums.Gender;
import lombok.*;

import java.time.LocalDate;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProfileDetailsReadOnlyDTO {
    private LocalDate dateOfBirth;
    private Gender gender;
}
