package gr.aueb.cf.spot_a_bird_app.dto.stats;

import lombok.Data;
import lombok.Builder;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class UserActivityStatsDTO {
    private long totalUsers;
    private long activeUsers; // Users with logs
    private List<UserActivityDTO> mostActiveUsers;


}
