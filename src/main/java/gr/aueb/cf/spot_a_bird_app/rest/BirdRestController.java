package gr.aueb.cf.spot_a_bird_app.rest;

import gr.aueb.cf.spot_a_bird_app.dto.BirdReadOnlyDTO;
import gr.aueb.cf.spot_a_bird_app.service.BirdService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/birds")
public class BirdRestController {
    private final BirdService birdService;

    public BirdRestController(BirdService birdService) {
        this.birdService = birdService;
    }

    @GetMapping("/for-selection")
    public List<BirdReadOnlyDTO> getBirdsForSelection() {
        return birdService.getAllBirds();
    }

}
