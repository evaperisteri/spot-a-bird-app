package gr.aueb.cf.spot_a_bird_app.dto.stats;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FamilyCountDTO {
    private Long familyId;
    private String familyName;
    private Long birdCount;
    private Long observationCount;
}