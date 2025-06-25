package gr.aueb.cf.spot_a_bird_app.dto;

import gr.aueb.cf.spot_a_bird_app.model.Bird;
import gr.aueb.cf.spot_a_bird_app.model.Region;
import gr.aueb.cf.spot_a_bird_app.model.User;
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
public class BirdwatchingLogInsertDTO {
    @NotNull(message = "Bird species is required")
    private Bird bird;
    @NotEmpty(message = "Quantity is required")
    private int quantity;
    @NotNull(message = "Region is required")
    private Region region;
    @NotNull(message = "User is required")
    private User user;
}
