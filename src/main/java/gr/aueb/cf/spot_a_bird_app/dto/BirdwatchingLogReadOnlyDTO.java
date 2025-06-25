package gr.aueb.cf.spot_a_bird_app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BirdwatchingLogReadOnlyDTO {
    private Long id;
    private BirdReadOnlyDTO bird;
    private int quantity;
    private RegionReadOnlyDTO region;
    private UserReadOnlyDTO user;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}