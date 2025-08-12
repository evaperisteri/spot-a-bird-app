package gr.aueb.cf.spot_a_bird_app.dto.stats;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class BirdCountDTO {
    private Long birdId;
    private String birdName;
    private long observationCount;
}