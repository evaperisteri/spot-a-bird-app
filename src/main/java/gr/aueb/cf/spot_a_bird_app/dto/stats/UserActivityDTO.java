package gr.aueb.cf.spot_a_bird_app.dto.stats;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserActivityDTO {
    private Long userId;
    private String username;
    private long logCount;
    private LocalDateTime lastActivity;
}
