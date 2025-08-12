package gr.aueb.cf.spot_a_bird_app.dto.stats;

import lombok.Data;
import lombok.Builder;
import java.util.List;

@Data
@Builder
public class UserLogStatisticsDTO {
    private long totalLogs;
    private long totalSpeciesObserved;
    private long totalRegionsVisited;
    private List<BirdCountDTO> mostSpottedBirds;
}
