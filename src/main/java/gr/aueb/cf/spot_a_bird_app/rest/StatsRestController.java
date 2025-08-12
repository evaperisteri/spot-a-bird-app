package gr.aueb.cf.spot_a_bird_app.rest;

import gr.aueb.cf.spot_a_bird_app.dto.stats.BirdStatisticsDTO;
import gr.aueb.cf.spot_a_bird_app.dto.stats.UserLogStatisticsDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class StatsRestController {
    private final StatisticsService statisticsService;
    @GetMapping("/birds")
    public ResponseEntity<BirdStatisticsDTO> getBirdStatistics() {
        return ResponseEntity.ok(statisticsService.getBirdStatistics());
    }

    @GetMapping("/user-logs")
    public ResponseEntity<UserLogStatisticsDTO> getUserLogStatistics() {
        return ResponseEntity.ok(statisticsService.getUserLogStatistics());
    }
}
