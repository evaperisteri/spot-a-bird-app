package gr.aueb.cf.spot_a_bird_app.rest;

import gr.aueb.cf.spot_a_bird_app.dto.BirdFullDetailsDTO;
import gr.aueb.cf.spot_a_bird_app.model.Bird;
import gr.aueb.cf.spot_a_bird_app.repository.BirdRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/birds")
@RequiredArgsConstructor
public class BirdRestController {

    private final BirdRepository birdRepository;

    @GetMapping
    public ResponseEntity<Page<BirdFullDetailsDTO>> getAllBirds(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {

        Page<Bird> birds = birdRepository.findAll(PageRequest.of(page, size));

        Page<BirdFullDetailsDTO> dtos = birds.map(b -> new BirdFullDetailsDTO(
                b.getId(),
                b.getName(),
                b.getScientificName(),
                b.getFamily().getName(),
                b.getImageUrl()));

        return ResponseEntity.ok(dtos);
    }

    /**
     Initial load(first page)
     *GET /api/birds?size=50
     Subsequent loads (lazy loading)
     *GET /api/birds?page=1&size=50
     */

    @GetMapping("/search")
    public ResponseEntity<List<BirdFullDetailsDTO>> searchBirds(
            @RequestParam(required = false) String query,
            @RequestParam(defaultValue = "10") int limit) {

        List<Bird> birds;
        if (query == null || query.isBlank()) {
            // Return random subset for initial display
            birds = birdRepository.findRandomBirds(limit); // Custom query
        } else {
            // Filtered search
            birds = birdRepository.findByNameOrScientificNameContaining(query, PageRequest.of(0, limit));
        }

        List<BirdFullDetailsDTO> results = birds.stream()
                .map(b -> new BirdFullDetailsDTO(
                        b.getId(),
                        b.getName(),
                        b.getScientificName(),
                        b.getFamily().getName(),
                        b.getImageUrl()))
                .toList();

        return ResponseEntity.ok(results);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BirdFullDetailsDTO> getBirdById(@PathVariable Long id) {
        return birdRepository.findById(id)
                .map(b -> new BirdFullDetailsDTO(
                        b.getId(),
                        b.getName(),
                        b.getScientificName(),
                        b.getFamily().getName(),
                        b.getImageUrl()))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}


