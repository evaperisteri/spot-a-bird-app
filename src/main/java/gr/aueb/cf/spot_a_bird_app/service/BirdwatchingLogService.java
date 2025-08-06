package gr.aueb.cf.spot_a_bird_app.service;

import gr.aueb.cf.spot_a_bird_app.authentication.AuthenticationService;
import gr.aueb.cf.spot_a_bird_app.core.exceptions.AppObjectNotFoundException;
import gr.aueb.cf.spot_a_bird_app.core.filters.BirdWatchingLogFilters;
import gr.aueb.cf.spot_a_bird_app.core.filters.UserFilters;
import gr.aueb.cf.spot_a_bird_app.core.specifications.BirdwatchingLogSpecification;
import gr.aueb.cf.spot_a_bird_app.core.specifications.UserSpecification;
import gr.aueb.cf.spot_a_bird_app.dto.BirdReadOnlyDTO;
import gr.aueb.cf.spot_a_bird_app.dto.BirdwatchingLogInsertDTO;
import gr.aueb.cf.spot_a_bird_app.dto.BirdwatchingLogReadOnlyDTO;
import gr.aueb.cf.spot_a_bird_app.dto.UserReadOnlyDTO;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static gr.aueb.cf.spot_a_bird_app.core.specifications.BirdwatchingLogSpecification.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class BirdwatchingLogService {

    private final UserRepository userRepository;
    private final AuthenticationService authService;
    private final BirdRepository birdRepository;
    private final RegionRepository regionRepository;
    private final BirdwatchingLogRepository bWLogRepository;
    private final Mapper mapper;
    private final BirdService birdService;


    @Transactional
    public BirdwatchingLogReadOnlyDTO saveLog(BirdwatchingLogInsertDTO dto) throws AppObjectNotFoundException {
        validateLogDTO(dto);
        User spotter = getAuthenticatedUser();

        Bird spottedBird = birdService.findBirdByName(dto.getBirdName());
        Region region = findRegionByName(dto.getRegionName());

        BirdwatchingLog log = buildLogEntity(dto, spotter, spottedBird, region);
        bWLogRepository.save(log);

        return mapper.mapBWLToReadOnlyDTO(log);
    }

//    private Bird findBirdByName(String name) throws AppObjectNotFoundException {
//        Optional<Bird> exactMatch = birdRepository.findByName(name);
//        if (exactMatch.isPresent()) {
//            return exactMatch.get();
//        }
//
//        List<Bird> partialMatches = birdRepository.findByNameContainingIgnoreCase(name);
//        if (!partialMatches.isEmpty()) {
//            return partialMatches.get(0); // Return first match
//        }
//
//        throw new AppObjectNotFoundException("Bird", "Bird not found with name: " + name);
//    }

    private Region findRegionByName(String name) throws AppObjectNotFoundException {
        return regionRepository.findByName(name)
                .orElseThrow(() -> new AppObjectNotFoundException("Region not found: ", name));
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

    private User getAuthenticatedUser() throws AppObjectNotFoundException {
        String username = authService.getAuthenticatedUsername();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new AppObjectNotFoundException("User not found: ", username));
    }


    @Transactional(readOnly = true)
    public List<BirdReadOnlyDTO> getAllBirdsForSelection() {
        return birdRepository.findAll().stream()
                .map(bird -> new BirdReadOnlyDTO(
                        bird.getId(),
                        bird.getName(),
                        bird.getScientificName(),
                        bird.getFamily() != null ? mapper.mapToFamilyReadOnlyDTO(bird.getFamily()) : null
                ))
                .sorted(Comparator.<BirdReadOnlyDTO, String>comparing(BirdReadOnlyDTO::getName))
                .toList();
    }

    //fixed sorting
    @Transactional
    public Page<BirdwatchingLogReadOnlyDTO> getLogsPaginated(int page, int size) {
        String defaultSort = "user";
        Pageable pageable = PageRequest.of(page, size, Sort.by(defaultSort).ascending());
        return bWLogRepository.findAll(pageable).map(mapper::mapBWLToReadOnlyDTO);
    }

    //dynamic sorting
    @Transactional
    public Page<BirdwatchingLogReadOnlyDTO> getLogsPaginatedAndSorted(int page, int size, String sortBy, String sortDirection){
        Sort.Direction direction = "desc".equalsIgnoreCase(sortDirection)
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return bWLogRepository.findAll(pageable).map(mapper::mapBWLToReadOnlyDTO);
    }

    @Transactional
    public List<BirdwatchingLogReadOnlyDTO> getLogsFiltered (BirdWatchingLogFilters filters) {
        return bWLogRepository.findAll(getSpecsFromFilters(filters)).stream().map(mapper::mapBWLToReadOnlyDTO).collect(Collectors.toList());
    }

    @Transactional
    public Page<BirdwatchingLogReadOnlyDTO> getLogsFilteredAndPaginated(
            BirdWatchingLogFilters filters,
            int page,
            int size,
            String sortBy,
            String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return bWLogRepository.findAll(getSpecsFromFilters(filters), pageable)
                .map(mapper::mapBWLToReadOnlyDTO);
    }

    public Specification<BirdwatchingLog> getSpecsFromFilters(BirdWatchingLogFilters filters) {
        return Specification
                .where(birdNameContains(filters.getName()))
                .and(birdScientificNameContains(filters.getScientificName()))
                .and(birdIdEquals(filters.getBirdId()))
                .and(regionNameContains(filters.getRegionName()))
                .and(regionIdEquals(filters.getRegionId()))
                .and(usernameContains(filters.getUsername()))
                .and(userIdEquals(filters.getUserId()))
                .and(dateEquals(filters.getDate()));
    }

    @Transactional(readOnly = true)
    public BirdwatchingLogReadOnlyDTO getLogById(Long id) throws AppObjectNotFoundException {
        return bWLogRepository.findById(id)
                .map(mapper::mapBWLToReadOnlyDTO)
                .orElseThrow(() -> new AppObjectNotFoundException("Bwl", "Birdwatching log not found with id: " + id));
    }

    @Transactional
    public BirdwatchingLogReadOnlyDTO updateLog(Long id, BirdwatchingLogInsertDTO updateDTO)
            throws AppObjectNotFoundException {
        BirdwatchingLog existingLog = bWLogRepository.findById(id)
                .orElseThrow(() -> new AppObjectNotFoundException("Bwl", "Log not found with id: " + id));

        Bird bird = null;
        if (updateDTO.getBirdName() != null) {
            bird = birdService.findBirdByName(updateDTO.getBirdName());
        }

        if (updateDTO.getQuantity() > 0) {
            existingLog.setQuantity(updateDTO.getQuantity());
        }

        Region region = null;
        if (updateDTO.getRegionName() != null) {
            region = findRegionByName(updateDTO.getRegionName());
        }

        mapper.updateBWLFromDto(updateDTO, existingLog, bird, region);
        return mapper.mapBWLToReadOnlyDTO(bWLogRepository.save(existingLog));
    }

    @Transactional
    public void deleteLog(Long id) throws AppObjectNotFoundException {
        if (!bWLogRepository.existsById(id)) {
            throw new AppObjectNotFoundException("Bwl", "Log not found with id: " + id);
        }
        bWLogRepository.deleteById(id);
    }
}
