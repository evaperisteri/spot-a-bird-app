package gr.aueb.cf.spot_a_bird_app.service;

import gr.aueb.cf.spot_a_bird_app.authentication.AuthenticationService;
import gr.aueb.cf.spot_a_bird_app.core.exceptions.AppObjectNotFoundException;
import gr.aueb.cf.spot_a_bird_app.dto.BirdReadOnlyDTO;
import gr.aueb.cf.spot_a_bird_app.dto.BirdwatchingLogInsertDTO;
import gr.aueb.cf.spot_a_bird_app.dto.BirdwatchingLogReadOnlyDTO;
import gr.aueb.cf.spot_a_bird_app.mapper.Mapper;
import gr.aueb.cf.spot_a_bird_app.model.Bird;
import gr.aueb.cf.spot_a_bird_app.model.BirdwatchingLog;
import gr.aueb.cf.spot_a_bird_app.model.Region;
import gr.aueb.cf.spot_a_bird_app.model.User;
import gr.aueb.cf.spot_a_bird_app.repository.BirdRepository;
import gr.aueb.cf.spot_a_bird_app.repository.BirdwatchingLogRepository;
import gr.aueb.cf.spot_a_bird_app.repository.RegionRepository;
import gr.aueb.cf.spot_a_bird_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BirdwatchingLogService {
    private final BirdwatchingLogRepository logRepository;
    private final UserRepository userRepository;
    private final AuthenticationService authService;
    private final BirdRepository birdRepository;
    private final RegionRepository regionRepository;
    private final Mapper mapper;

    @Transactional
    public BirdwatchingLogReadOnlyDTO saveLog(BirdwatchingLogInsertDTO dto) {
        validateLogDTO(dto);
        User spotter = getAuthenticatedUser();

        Bird spottedBird = findBirdByName(dto.getBirdName());
        Region region = findRegionByName(dto.getRegionName());

        BirdwatchingLog log = buildLogEntity(dto, spotter, spottedBird, region);
        logRepository.save(log);

        return mapper.mapToReadOnlyDTO(log);
    }

    private Bird findBirdByName(String name) {
        // Exact match first
        return birdRepository.findByName(name)
                .or(() -> birdRepository.findByNameContainingIgnoreCase(name))
                .orElseThrow(() -> new AppObjectNotFoundException("Bird not found with name: " + name));
    }

    private Region findRegionByName(String name) {
        return regionRepository.findByName(name)
                .orElseThrow(() -> new AppObjectNotFoundException("Region not found: " + name));
    }

    private void validateLogDTO(BirdwatchingLogInsertDTO dto) {
        if (dto.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        if (dto.getBirdName() == null) {
            throw new IllegalArgumentException("Bird selection is required");
        }
        if (dto.getRegionName() == null) {
            throw new IllegalArgumentException("Region selection is required");
        }
    }

    private BirdwatchingLog buildLogEntity(BirdwatchingLogInsertDTO dto, User spotter, Bird bird, Region region) {
        return BirdwatchingLog.builder()
                .bird(bird)
                .region(region)
                .user(spotter)
                .quantity(dto.getQuantity())
                .build();
    }

    private User getAuthenticatedUser() {
        String username = authService.getAuthenticatedUsername();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new AppObjectNotFoundException("User not found: " + username));
    }

    // New method to get birds for combo box
    @Transactional(readOnly = true)
    public List<BirdReadOnlyDTO> getAllBirdsForSelection() {
        return birdRepository.findAll().stream()
                .map(bird -> new BirdReadOnlyDTO(
                        bird.getId(),
                        bird.getName(),
                        bird.getScientificName()
                ))
                .sorted(Comparator.comparing(BirdReadOnlyDTO::getName))
                .toList();
    }
}
