package gr.aueb.cf.spot_a_bird_app.dto.stats;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BirdCountDTO {
    private Long birdId;
    private String birdName;
    private Long observationCount;
}