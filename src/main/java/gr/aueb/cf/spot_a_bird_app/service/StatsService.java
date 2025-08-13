package gr.aueb.cf.spot_a_bird_app.service;

import gr.aueb.cf.spot_a_bird_app.authentication.AuthenticationService;
import gr.aueb.cf.spot_a_bird_app.core.exceptions.AppObjectNotFoundException;
import gr.aueb.cf.spot_a_bird_app.dto.stats.*;
import gr.aueb.cf.spot_a_bird_app.repository.BirdRepository;
import gr.aueb.cf.spot_a_bird_app.repository.BirdwatchingLogRepository;
import gr.aueb.cf.spot_a_bird_app.repository.FamilyRepository;
import gr.aueb.cf.spot_a_bird_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatsService {
    private final BirdRepository birdRepository;
    private final BirdwatchingLogRepository logRepository;
    private final FamilyRepository familyRepository;
    private final AuthenticationService authService;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public BirdStatisticsDTO getBirdStatistics() {
        return BirdStatisticsDTO.builder()
                .totalSpecies(birdRepository.count())
                .totalObservations(logRepository.count())
                .totalFamilies(familyRepository.count())
                .topFamilies(birdRepository.findTopFamiliesWithCounts(PageRequest.of(0, 5)))
                .regionsWithMostObservations(
                        logRepository.findTopRegionsByObservations(PageRequest.of(0, 3)))
                .build();
    }

    @Transactional(readOnly = true)
    public UserLogStatisticsDTO getUserLogStatistics() throws AppObjectNotFoundException {
        String username = authService.getAuthenticatedUsername();
        long totalLogs = logRepository.countByUserUsername(username);
        long totalSpecies = logRepository.countDistinctBirdsByUser(username);
        long totalRegions = logRepository.countDistinctRegionsByUser(username);

        List<BirdCountDTO> topBirds = logRepository.findTopBirdsByUser(username, PageRequest.of(0, 3));

        return UserLogStatisticsDTO.builder()
                .totalLogs(totalLogs)
                .totalSpeciesObserved(totalSpecies)
                .totalRegionsVisited(totalRegions)
                .mostSpottedBirds(topBirds)
                .build();
    }

    @Transactional(readOnly = true)
    public List<RegionCountDTO> getRegionStatistics() {
        return logRepository.findTopRegionsByObservations(PageRequest.of(0, 5));
    }

//    @Transactional(readOnly = true)
//    public UserActivityStatsDTO getUserActivityStats() {
//        return UserActivityStatsDTO.builder()
//                .totalUsers(userRepository.countTotalUsers())
//                .activeUsers(userRepository.countActiveUsers())
//                .mostActiveUsers(userRepository.findMostActiveUsers(PageRequest.of(0, 5)))
//                .build();
//    }

    @Transactional(readOnly = true)
    public FamilyStatisticsDTO getFamilyStats(int topCount) {
        PageRequest page = PageRequest.of(0, topCount);

        return FamilyStatisticsDTO.builder()
                .totalFamilies(familyRepository.count())
                .familiesWithMostSpecies(familyRepository.findFamiliesBySpeciesCount(page))
                .familiesWithMostObservations(familyRepository.findFamiliesByObservationCount(page))
                .build();
    }

    @Transactional(readOnly = true)
    public Map<String, Long> getSpeciesDistribution() {
        return birdRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        bird -> bird.getFamily() != null ? bird.getFamily().getName() : "Unknown",
                        Collectors.counting()
                ));
    }
}
