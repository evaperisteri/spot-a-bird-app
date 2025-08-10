package gr.aueb.cf.spot_a_bird_app.rest;

import gr.aueb.cf.spot_a_bird_app.authentication.AuthenticationService;
import gr.aueb.cf.spot_a_bird_app.core.exceptions.*;
import gr.aueb.cf.spot_a_bird_app.core.filters.BirdWatchingLogFilters;
import gr.aueb.cf.spot_a_bird_app.dto.BirdwatchingLogInsertDTO;
import gr.aueb.cf.spot_a_bird_app.dto.BirdwatchingLogReadOnlyDTO;
import gr.aueb.cf.spot_a_bird_app.mapper.Mapper;
import gr.aueb.cf.spot_a_bird_app.model.BirdwatchingLog;
import gr.aueb.cf.spot_a_bird_app.repository.BirdwatchingLogRepository;
import gr.aueb.cf.spot_a_bird_app.service.BirdwatchingLogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/api/bwlogs")
@RequiredArgsConstructor
public class BirdwatchingLogRestController {

    private final Logger LOGGER = LoggerFactory.getLogger(BirdwatchingLogRestController.class);
    private final BirdwatchingLogService bwlService;
    private final AuthenticationService authService;
    private final BirdwatchingLogRepository bWLogRepository;
    private final Mapper mapper;


    @PostMapping("/save")
    public ResponseEntity<BirdwatchingLogReadOnlyDTO> saveBWLog(
            @Valid @RequestBody BirdwatchingLogInsertDTO bwlInsertDTO,
            BindingResult bindingResult)
            throws ValidationException, AppObjectNotFoundException {

        // Validating the input
        if (bindingResult.hasErrors()) {
            LOGGER.warn("Validation errors: {}", bindingResult.getAllErrors());
            throw new ValidationException(bindingResult);
        }

        try {
            BirdwatchingLogReadOnlyDTO savedLog = bwlService.saveLog(bwlInsertDTO);
            return ResponseEntity.ok(savedLog);
        } catch (AppObjectNotFoundException e) {
            LOGGER.error("Failed to save log: {}", e.getMessage());
            throw e;
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<BirdwatchingLogReadOnlyDTO> getLogById(@PathVariable Long id)
            throws AppObjectNotFoundException {
        return ResponseEntity.ok(bwlService.getLogById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLog(@PathVariable Long id)
            throws AppObjectNotFoundException {
        bwlService.deleteLog(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<BirdwatchingLogReadOnlyDTO> updateLog(
            @PathVariable Long id,
            @Valid @RequestBody BirdwatchingLogInsertDTO updateDTO,
            BindingResult bindingResult)
            throws AppObjectNotFoundException, ValidationException {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }
        return ResponseEntity.ok(bwlService.updateLog(id, updateDTO));
    }


    @GetMapping("/paginated")
    public ResponseEntity<Page<BirdwatchingLogReadOnlyDTO>> getPaginatedLogs(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        Page<BirdwatchingLogReadOnlyDTO> logsPage = bwlService.getLogsPaginated(page, size);
        return new ResponseEntity<>(logsPage, HttpStatus.OK);
    }

    @PostMapping("/filtered")
    public ResponseEntity<List<BirdwatchingLogReadOnlyDTO>> getFilteredLogs(@Nullable @RequestBody BirdWatchingLogFilters filters) throws AppObjectNotAuthorizedException {
        if (filters==null)filters = BirdWatchingLogFilters.builder().build();
        return ResponseEntity.ok(bwlService.getLogsFiltered(filters));
    }

    @PostMapping("/filtered/paginated")
    public ResponseEntity<Page<BirdwatchingLogReadOnlyDTO>> getFilteredPaginatedLogs
            (@Nullable @RequestBody BirdWatchingLogFilters filters,
             @RequestParam(defaultValue = "0") int page,
             @RequestParam(defaultValue = "10") int size,
             @RequestParam(defaultValue = "observationDate") String sortBy,
             @RequestParam(defaultValue = "ASC") String sortDirection) throws AppObjectNotAuthorizedException {
        if (filters==null) filters = BirdWatchingLogFilters.builder().build();
        return ResponseEntity.ok(bwlService.getLogsFilteredAndPaginated(filters, page, size, sortBy, sortDirection));
    }

    @GetMapping("/my-logs")
    @PreAuthorize("hasRole('SPOTTER') or hasRole('ADMIN')")  // Requires authentication
    public ResponseEntity<Page<BirdwatchingLogReadOnlyDTO>> getMyLogs(
            @PageableDefault(sort = "observationDate", direction = Sort.Direction.DESC) Pageable pageable) throws AppObjectNotFoundException {
        return ResponseEntity.ok(bwlService.getLogsForCurrentUser(pageable));
    }

    @PostMapping("/my-logs/filtered")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Page<BirdwatchingLogReadOnlyDTO>> getMyFilteredLogs(
            @RequestBody(required = false) BirdWatchingLogFilters filters,
            @PageableDefault(sort = "observationDate", direction = Sort.Direction.DESC) Pageable pageable) throws AppObjectNotFoundException {

        if (filters == null) {
            filters = new BirdWatchingLogFilters(); // Initialize empty filters
        }

        return ResponseEntity.ok(bwlService.getMyLogsFiltered(filters, pageable));
    }
}
