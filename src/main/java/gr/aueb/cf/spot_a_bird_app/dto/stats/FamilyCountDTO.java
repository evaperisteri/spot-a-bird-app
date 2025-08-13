package gr.aueb.cf.spot_a_bird_app.dto.stats;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FamilyCountDTO {
    private Long familyId;
    private String familyName;
    private Long birdCount;
    private Long observationCount;
}