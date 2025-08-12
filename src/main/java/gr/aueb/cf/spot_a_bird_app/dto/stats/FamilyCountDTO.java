package gr.aueb.cf.spot_a_bird_app.dto.stats;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class FamilyCountDTO {
    private Long familyId;
    private String familyName;
    private long birdCount;
    private long observationCount;
}