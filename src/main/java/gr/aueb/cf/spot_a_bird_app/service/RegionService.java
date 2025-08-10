package gr.aueb.cf.spot_a_bird_app.service;

import gr.aueb.cf.spot_a_bird_app.core.exceptions.AppObjectNotFoundException;
import gr.aueb.cf.spot_a_bird_app.dto.RegionReadOnlyDTO;
import gr.aueb.cf.spot_a_bird_app.mapper.Mapper;
import gr.aueb.cf.spot_a_bird_app.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RegionService {
    private final Mapper mapper;
    private final RegionRepository regionRepository;

    public List<RegionReadOnlyDTO> getAllRegions() {
        return regionRepository.findAll()
                .stream()
                .map(mapper::mapToRegionReadOnlyDTO)
                .collect(Collectors.toList());
    }

    public RegionReadOnlyDTO getRegionById(Long id) throws AppObjectNotFoundException {
        return regionRepository
                .findById(id)
                .map(mapper::mapToRegionReadOnlyDTO)
                .orElseThrow(()-> new AppObjectNotFoundException("Region", "Region with id:" + id + " not found"));
    }
}
