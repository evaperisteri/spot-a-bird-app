package gr.aueb.cf.spot_a_bird_app.core.filters;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.lang.Nullable;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class UserFilters extends GenericFilters {
    @Nullable
    private Long id;
    @Nullable
    private Boolean isActive;
}
