package gr.aueb.cf.spot_a_bird_app.rest;

import gr.aueb.cf.spot_a_bird_app.repository.BirdRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/birds")
@RequiredArgsConstructor
public class BirdRestController {

    private final BirdRepository birdRepository;


    @GetMapping("/search")
    public ResponseEntity<List<BirdSearchResultDTO>> searchBirds(
            @RequestParam String query,
            @RequestParam(defaultValue = "10") int limit) {

        List<BirdSearchResultDTO> results = birdRepository
                .findByNameContainingIgnoreCase(query, PageRequest.of(0, limit))
                .stream()
                .map(bird -> new BirdSearchResultDTO(
                        bird.getId(),
                        bird.getName(),
                        bird.getScientificName()))
                .toList();

        return ResponseEntity.ok(results);
    }

    public record BirdSearchResultDTO(
            Long id,
            String commonName,
            String scientificName
    ) {
        public String getDisplayText() {
            return String.format("%s (%s)", commonName, scientificName);
        }
    }
}


