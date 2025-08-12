package gr.aueb.cf.spot_a_bird_app.service;

import gr.aueb.cf.spot_a_bird_app.authentication.AuthenticationService;
import gr.aueb.cf.spot_a_bird_app.core.exceptions.AppObjectNotFoundException;
import gr.aueb.cf.spot_a_bird_app.dto.stats.BirdCountDTO;
import gr.aueb.cf.spot_a_bird_app.dto.stats.BirdStatisticsDTO;
import gr.aueb.cf.spot_a_bird_app.dto.stats.UserLogStatisticsDTO;
import gr.aueb.cf.spot_a_bird_app.repository.BirdRepository;
import gr.aueb.cf.spot_a_bird_app.repository.BirdwatchingLogRepository;
import gr.aueb.cf.spot_a_bird_app.repository.FamilyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsService {
    private final BirdRepository birdRepository;
    private final BirdwatchingLogRepository logRepository;
    private final FamilyRepository familyRepository;
    private final AuthenticationService authService;

    @Transactional(readOnly = true)
    public BirdStatisticsDTO getBirdStatistics() {
        return BirdStatisticsDTO.builder()
                .totalSpecies(birdRepository.count())
                .totalObservations(logRepository.count())
                .totalFamilies(familyRepository.count())
                .topFamilies(familyRepository.findTopFamiliesWithCounts(PageRequest.of(0, 5)))
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
}
