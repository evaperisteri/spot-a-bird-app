package gr.aueb.cf.spot_a_bird_app.dto.stats;

import lombok.Data;
import lombok.Builder;
import java.util.List;

@Data
@Builder
public class UserLogStatisticsDTO {
    private Long totalLogs;
    private Long totalSpeciesObserved;
    private Long totalRegionsVisited;
    private List<BirdCountDTO> mostSpottedBirds;
}
