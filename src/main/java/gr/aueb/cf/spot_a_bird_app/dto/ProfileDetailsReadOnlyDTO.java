package gr.aueb.cf.spot_a_bird_app.dto;

import gr.aueb.cf.spot_a_bird_app.core.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProfileDetailsReadOnlyDTO {
    private LocalDate dateOfBirth;
    private Gender gender;
}
