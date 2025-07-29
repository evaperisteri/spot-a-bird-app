package gr.aueb.cf.spot_a_bird_app.service;

import gr.aueb.cf.spot_a_bird_app.authentication.AuthenticationService;
import gr.aueb.cf.spot_a_bird_app.core.exceptions.AppObjectNotFoundException;
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
    public BirdwatchingLogReadOnlyDTO createLog(BirdwatchingLogInsertDTO dto) {

        User spotter = getAuthenticatedUser();

        Bird spottedBird = birdRepository.findByName(dto.getBirdName()).orElseGet(()->createNewBird(dto.getBirdName()));

        Region region = regionRepository.findByName(dto.getRegionName()).orElseThrow(()->new AppObjectNotFoundException("region not found", dto.getRegionName()));

        if (dto.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }

        BirdwatchingLog log = BirdwatchingLog.builder()
                .bird(spottedBird)
                .region(region)
                .user(spotter)
                .quantity(dto.getQuantity())
                .build();

        logRepository.save(log);


        // 7. Return mapped DTO
        return mapper.mapToReadOnlyDTO(log);
    }

    private User getAuthenticatedUser() {
        String username = authService.getAuthenticatedUsername();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new AppObjectNotFoundException("User not found: " , username));
    }
}
