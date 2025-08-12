package gr.aueb.cf.spot_a_bird_app.rest;

import gr.aueb.cf.spot_a_bird_app.core.exceptions.AppObjectNotFoundException;
import gr.aueb.cf.spot_a_bird_app.dto.stats.*;
import gr.aueb.cf.spot_a_bird_app.service.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class StatsRestController {
    private final StatsService statsService;
    @GetMapping("/birds")
    public ResponseEntity<BirdStatisticsDTO> getBirdStatistics() {
        return ResponseEntity.ok(statsService.getBirdStatistics());
    }

    @GetMapping("/user-logs")
    public ResponseEntity<UserLogStatisticsDTO> getUserLogStatistics() throws AppObjectNotFoundException {
        return ResponseEntity.ok(statsService.getUserLogStatistics());
    }

    @GetMapping("/regions")
    public ResponseEntity<List<RegionCountDTO>> getRegionStatistics() throws AppObjectNotFoundException {
        return ResponseEntity.ok(statsService.getBirdStatistics().getRegionsWithMostObservations());
    }

    @GetMapping("/users")
    public ResponseEntity<UserActivityStatsDTO> getUserActivityStats() {
        return ResponseEntity.ok(statsService.getUserActivityStats());
    }

    @GetMapping("/families")
    public ResponseEntity<FamilyStatisticsDTO> getFamilyStats() {
        return ResponseEntity.ok(statsService.getFamilyStats());
    }

    @GetMapping("/species-distribution")
    public ResponseEntity<Map<String, Long>> getSpeciesDistribution() {
        return ResponseEntity.ok(statsService.getSpeciesDistribution());
    }
}
