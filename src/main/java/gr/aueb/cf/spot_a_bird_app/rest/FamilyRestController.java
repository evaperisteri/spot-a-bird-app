package gr.aueb.cf.spot_a_bird_app.rest;

import gr.aueb.cf.spot_a_bird_app.dto.FamilyReadOnlyDTO;
import gr.aueb.cf.spot_a_bird_app.mapper.Mapper;
import gr.aueb.cf.spot_a_bird_app.repository.FamilyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
