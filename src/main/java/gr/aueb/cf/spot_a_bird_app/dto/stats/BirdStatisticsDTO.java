package gr.aueb.cf.spot_a_bird_app.dto.stats;

import lombok.Data;
import lombok.Builder;
import java.util.List;

@Data
@Builder
public class BirdStatisticsDTO {
    private long totalSpecies;
    private long totalObservations;
    private long totalFamilies;
    private List<FamilyCountDTO> topFamilies;
    private List<RegionCountDTO> regionsWithMostObservations;
}
