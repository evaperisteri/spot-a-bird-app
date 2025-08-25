package gr.aueb.cf.spot_a_bird_app.service;

import gr.aueb.cf.spot_a_bird_app.core.exceptions.AppObjectNotFoundException;
import gr.aueb.cf.spot_a_bird_app.dto.BirdInsertDTO;
import gr.aueb.cf.spot_a_bird_app.dto.BirdReadOnlyDTO;
import gr.aueb.cf.spot_a_bird_app.mapper.Mapper;
import gr.aueb.cf.spot_a_bird_app.model.Bird;
import gr.aueb.cf.spot_a_bird_app.model.Family;
import gr.aueb.cf.spot_a_bird_app.repository.BirdRepository;
import gr.aueb.cf.spot_a_bird_app.repository.FamilyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BirdService {
    private final BirdRepository birdRepository;
    private final FamilyRepository familyRepository;
    private final Mapper mapper;

    // Get all birds
    public List<BirdReadOnlyDTO> getAllBirds() {
        return birdRepository.findAll().stream()
                .map(bird -> new BirdReadOnlyDTO(bird.getId(), bird.getName(), bird.getScientificName(), mapper.mapToFamilyReadOnlyDTO(bird.getFamily()), bird.getImageUrl()))
                .sorted(Comparator.comparing(BirdReadOnlyDTO::getName))
                .toList();
    }

    @Transactional
    public BirdReadOnlyDTO saveBird(BirdInsertDTO dto) throws AppObjectNotFoundException {
        Family family = familyRepository.findByName(dto.getFamily().getName())
                .orElseThrow(() -> new AppObjectNotFoundException("Family", "Family not found"));

        Bird bird = new Bird();
        bird.setName(dto.getName());
        bird.setScientificName(dto.getScientificName());
        bird.setFamily(family);

        return mapper.mapToBirdReadOnlyDTO(birdRepository.save(bird));
    }

    @Transactional(readOnly = true)
    public List<BirdReadOnlyDTO> getBirdsByFamily(Long familyId) {
        return birdRepository.findByFamilyId(familyId).stream()
                .map(mapper::mapToBirdReadOnlyDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<BirdReadOnlyDTO> getBirdsByFamilyName(String familyName) {
        return birdRepository.findByFamilyName(familyName).stream()
                .map(mapper::mapToBirdReadOnlyDTO)
                .toList();
    }

    public Bird findBirdByName(String name) throws AppObjectNotFoundException {
        Optional<Bird> exactMatch = birdRepository.findByName(name);
        if (exactMatch.isPresent()) {
            return exactMatch.get();
        }

        List<Bird> partialMatches = birdRepository.findByNameContainingIgnoreCase(name);
        if (!partialMatches.isEmpty()) {
            return partialMatches.get(0); // Return first match
        }

        throw new AppObjectNotFoundException("Bird", "Bird not found with name: " + name);
    }
}
