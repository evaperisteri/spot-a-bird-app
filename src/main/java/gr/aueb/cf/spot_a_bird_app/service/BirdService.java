package gr.aueb.cf.spot_a_bird_app.service;

import gr.aueb.cf.spot_a_bird_app.dto.BirdReadOnlyDTO;
import gr.aueb.cf.spot_a_bird_app.repository.BirdRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class BirdService {
    private final BirdRepository birdRepository;

    public BirdService(BirdRepository birdRepository) {
        this.birdRepository = birdRepository;
    }

    // Get all birds
    public List<BirdReadOnlyDTO> getAllBirds() {
        return birdRepository.findAll().stream()
                .map(bird -> new BirdReadOnlyDTO(bird.getId(), bird.getName(), bird.getScientificName()))
                .sorted(Comparator.comparing(BirdReadOnlyDTO::getName))
                .toList();
    }
}
