package gr.aueb.cf.spot_a_bird_app.dto;

import jakarta.validation.constraints.Min;
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
    private String birdName;
    @Min(value=1, message = "Quantity must be at least 1")
    private int quantity;
    @NotNull(message = "Region is required")
    private String regionName;
}
