package gr.aueb.cf.spot_a_bird_app.core.filters;

import gr.aueb.cf.spot_a_bird_app.core.enums.Gender;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.lang.Nullable;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class UserFilters extends GenericFilters {

    private Long id;
    @Nullable
    private Boolean isActive;

    @Nullable
    private Gender gender;

    @Nullable
    private LocalDate dateOfBirth;
}
