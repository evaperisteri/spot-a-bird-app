package gr.aueb.cf.spot_a_bird_app.dto.stats;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BirdCountDTO {
    private Long birdId;
    private String birdName;
    private long observationCount;
}