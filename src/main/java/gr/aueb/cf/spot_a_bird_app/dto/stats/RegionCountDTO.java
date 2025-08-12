package gr.aueb.cf.spot_a_bird_app.dto.stats;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class RegionCountDTO {
    private Long regionId;
    private String regionName;
    private long observationCount;
}
