package gr.aueb.cf.spot_a_bird_app.mapper;

import gr.aueb.cf.spot_a_bird_app.dto.*;
import gr.aueb.cf.spot_a_bird_app.model.BirdwatchingLog;
import gr.aueb.cf.spot_a_bird_app.model.ProfileDetails;
import gr.aueb.cf.spot_a_bird_app.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Mapper {
    private final PasswordEncoder passwordEncoder;

    public UserReadOnlyDTO mapToUserReadOnlyDTO(User user) {
        UserReadOnlyDTO userReadOnlyDTO = new UserReadOnlyDTO();
        userReadOnlyDTO.setId(user.getId());
        userReadOnlyDTO.setEmail(user.getEmail());
        userReadOnlyDTO.setUsername(user.getEmail());
        userReadOnlyDTO.setFirstname(user.getFirstname());
        userReadOnlyDTO.setLastname(user.getLastname());
        userReadOnlyDTO.setRole(user.getRole());

        ProfileDetailsReadOnlyDTO profileDetailsReadOnlyDTO = new ProfileDetailsReadOnlyDTO();
        profileDetailsReadOnlyDTO.setGender(user.getProfileDetails().getGender());
        profileDetailsReadOnlyDTO.setDateOfBirth(user.getProfileDetails().getDateOfBirth());
        return userReadOnlyDTO;
    }

    public User mapToUser(UserInsertDTO userInsertDTO) {
        User user = new User();
        user.setFirstname(userInsertDTO.getFirstname());
        user.setLastname(userInsertDTO.getLastname());
        user.setEmail(userInsertDTO.getEmail());
        user.setRole(userInsertDTO.getRole());
        user.setIsActive(userInsertDTO.getIsActive());
        user.setUsername(userInsertDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userInsertDTO.getPassword()));


        ProfileDetailsInsertDTO profileDTO = userInsertDTO.getProfileDetailsInsertDTO();
        ProfileDetails profileDetails = new ProfileDetails();
        profileDetails.setGender(profileDTO.getGender());
        profileDetails.setDateOfBirth(profileDTO.getDateOfBirth());
        user.setProfileDetails(profileDetails);
        return user;
    }

    public BirdwatchingLogReadOnlyDTO mapBWLToReadOnlyDTO(BirdwatchingLog bwlog) {
        if (bwlog == null) return null;

        // 1. Handle nested Bird mapping
        BirdReadOnlyDTO birdDTO = null;
        if (bwlog.getBird() != null) {
            birdDTO = new BirdReadOnlyDTO();
            birdDTO.setId(bwlog.getBird().getId());
            birdDTO.setName(bwlog.getBird().getName());
        }

        // 2. Handle nested Region mapping
        RegionReadOnlyDTO regionDTO = null;
        if (bwlog.getRegion() != null) {
            regionDTO = new RegionReadOnlyDTO();
            regionDTO.setId(bwlog.getRegion().getId());
            regionDTO.setName(bwlog.getRegion().getName());
        }

        // 3. Handle nested User mapping (with ProfileDetails)
        UserReadOnlyDTO userDTO = null;
        if (bwlog.getUser() != null) {
            ProfileDetailsReadOnlyDTO profileDTO = null;
            if (bwlog.getUser().getProfileDetails() != null) {
                profileDTO = new ProfileDetailsReadOnlyDTO();
                profileDTO.setDateOfBirth(bwlog.getUser().getProfileDetails().getDateOfBirth());
                profileDTO.setGender(bwlog.getUser().getProfileDetails().getGender());
            }

            userDTO = new UserReadOnlyDTO();
            userDTO.setId(bwlog.getUser().getId());
            userDTO.setUsername(bwlog.getUser().getUsername());
            userDTO.setFirstname(bwlog.getUser().getFirstname());
            userDTO.setLastname(bwlog.getUser().getLastname());
            userDTO.setRole(bwlog.getUser().getRole());
            userDTO.setProfileDetails(profileDTO);
        }

        // 4. Build the main DTO
        return BirdwatchingLogReadOnlyDTO.builder()
                .id(bwlog.getId())
                .bird(birdDTO)
                .quantity(bwlog.getQuantity())
                .region(regionDTO)
                .user(userDTO)
                .createdAt(bwlog.getUpdatedAt())
                .updatedAt(bwlog.getCreatedAt())
                .build();
    }
}
