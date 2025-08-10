package gr.aueb.cf.spot_a_bird_app.rest;

import gr.aueb.cf.spot_a_bird_app.dto.FamilyReadOnlyDTO;
import gr.aueb.cf.spot_a_bird_app.mapper.Mapper;
import gr.aueb.cf.spot_a_bird_app.repository.FamilyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/families")
@RequiredArgsConstructor
public class FamilyRestController {

    private final FamilyRepository familyRepository;
    private final Mapper mapper;

    @GetMapping
    public List<FamilyReadOnlyDTO> getAllFamilies() {
        return familyRepository.findAll().stream()
                .map(mapper::mapToFamilyReadOnlyDTO)
                .toList();
    }
    @GetMapping("/paginated")
    public Page<FamilyReadOnlyDTO> getAllFamilies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        return familyRepository.findAll(PageRequest.of(page, size))
                .map(mapper::mapToFamilyReadOnlyDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FamilyReadOnlyDTO> getFamilyById(@PathVariable Long id) {
        return familyRepository.findById(id)
                .map(mapper::mapToFamilyReadOnlyDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
