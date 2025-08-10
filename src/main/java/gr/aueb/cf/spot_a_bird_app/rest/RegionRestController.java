package gr.aueb.cf.spot_a_bird_app.rest;

import gr.aueb.cf.spot_a_bird_app.dto.RegionReadOnlyDTO;
import gr.aueb.cf.spot_a_bird_app.model.Region;
import gr.aueb.cf.spot_a_bird_app.repository.RegionRepository;
import gr.aueb.cf.spot_a_bird_app.service.RegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/regions")
@RequiredArgsConstructor
public class RegionRestController {

    private final RegionService regionService;
    private final RegionRepository regionRepository;

    @GetMapping
    public ResponseEntity<List<RegionReadOnlyDTO>> getAllRegions() {
        List<RegionReadOnlyDTO> regions = regionService.getAllRegions();
        return ResponseEntity.ok(regions);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<RegionReadOnlyDTO>> filterRegions(@RequestParam String name) {

        List<Region> regions = regionRepository.findByNameContainingIgnoreCase(name);

        return ResponseEntity.ok(
                regions.stream()
                        .map(r -> new RegionReadOnlyDTO(r.getId(), r.getName()))
                        .toList()
        );
    }
}
