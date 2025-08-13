package gr.aueb.cf.spot_a_bird_app.dto.stats;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FamilyCountDTO {
    private Long familyId;
    private String familyName;
    private long birdCount;
    private long observationCount;

    public FamilyCountDTO(Long id, String name, Long birdCount, Long observationCount) {
        this.familyId = id;
        this.familyName = name;
        this.birdCount = birdCount;
        this.observationCount = observationCount;
    }
}