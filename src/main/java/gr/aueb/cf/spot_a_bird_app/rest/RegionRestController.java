package gr.aueb.cf.spot_a_bird_app.rest;

import gr.aueb.cf.spot_a_bird_app.dto.RegionReadOnlyDTO;
import gr.aueb.cf.spot_a_bird_app.service.RegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/regions")
@RequiredArgsConstructor
public class RegionRestController {

    private final RegionService regionService;

    @GetMapping
    public ResponseEntity<List<RegionReadOnlyDTO>> getAllRegions() {
        List<RegionReadOnlyDTO> regions = regionService.getAllRegions();
        return ResponseEntity.ok(regions);
    }
}
