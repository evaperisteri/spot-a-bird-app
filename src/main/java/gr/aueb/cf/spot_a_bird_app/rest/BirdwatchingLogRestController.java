package gr.aueb.cf.spot_a_bird_app.rest;

import gr.aueb.cf.spot_a_bird_app.core.exceptions.*;
import gr.aueb.cf.spot_a_bird_app.core.filters.BirdWatchingLogFilters;
import gr.aueb.cf.spot_a_bird_app.dto.BirdwatchingLogInsertDTO;
import gr.aueb.cf.spot_a_bird_app.dto.BirdwatchingLogReadOnlyDTO;
import gr.aueb.cf.spot_a_bird_app.service.BirdwatchingLogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/bwlogs")
@RequiredArgsConstructor
public class BirdwatchingLogRestController {

    private final BirdwatchingLogService bwlService;

    @PostMapping("/save")
    public ResponseEntity<BirdwatchingLogReadOnlyDTO> saveBWLog(@Valid BirdwatchingLogInsertDTO bwlInsertDTO,
                                                                            BindingResult bindingResult)
            throws AppObjectInvalidArgumentException, ValidationException, AppObjectAlreadyExists, AppServerException, IOException, AppObjectNotFoundException {
        if(bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }
        BirdwatchingLogReadOnlyDTO bwLogReadOnlyDTO = bwlService.saveLog(bwlInsertDTO);
        return new ResponseEntity<>(bwLogReadOnlyDTO, HttpStatus.OK);
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
        if (filters==null) BirdWatchingLogFilters.builder().build();
        return ResponseEntity.ok(bwlService.getLogsFiltered(filters));
    }

    @PostMapping("/filtered/paginated")
    public ResponseEntity<Page<BirdwatchingLogReadOnlyDTO>> getFilteredPaginatedLogs
            (@Nullable @RequestBody BirdWatchingLogFilters filters,
             @RequestParam(defaultValue = "0") int page,
             @RequestParam(defaultValue = "10") int size,
             @RequestParam(defaultValue = "observationDate") String sortBy,
             @RequestParam(defaultValue = "ASC") String sortDirection) throws AppObjectNotAuthorizedException {
        if (filters==null) BirdWatchingLogFilters.builder().build();
        return ResponseEntity.ok(bwlService.getLogsFilteredAndPaginated(filters, page, size, sortBy, sortDirection));
    }
}
