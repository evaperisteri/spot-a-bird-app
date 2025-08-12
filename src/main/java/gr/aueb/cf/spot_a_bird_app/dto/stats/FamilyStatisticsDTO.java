package gr.aueb.cf.spot_a_bird_app.dto.stats;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FamilyStatisticsDTO {
    private long totalFamilies;
    private List<FamilyCountDTO> familiesWithMostSpecies;
    private List<FamilyCountDTO> familiesWithMostObservations;
}