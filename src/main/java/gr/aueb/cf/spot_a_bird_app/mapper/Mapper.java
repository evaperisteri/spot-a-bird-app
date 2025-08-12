package gr.aueb.cf.spot_a_bird_app.mapper;

import gr.aueb.cf.spot_a_bird_app.dto.*;
import gr.aueb.cf.spot_a_bird_app.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Mapper {
    private final PasswordEncoder passwordEncoder;

    public UserReadOnlyDTO mapToUserReadOnlyDTO(User user) {

        return UserReadOnlyDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .role(user.getRole())
                .profileDetails(user.getProfileDetails() != null ?
                        ProfileDetailsReadOnlyDTO.builder()
                                .gender(user.getProfileDetails().getGender())
                                .dateOfBirth(user.getProfileDetails().getDateOfBirth())
                                .build()
                        : null)
                .build();
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
        if (profileDTO != null) {
            ProfileDetails profileDetails = new ProfileDetails();
            profileDetails.setGender(profileDTO.getGender());
            profileDetails.setDateOfBirth(profileDTO.getDateOfBirth());
            profileDetails.setUser(user);
            user.setProfileDetails(profileDetails);
        }
        return user;
    }

    public BirdwatchingLogReadOnlyDTO mapBWLToReadOnlyDTO(BirdwatchingLog bwlog) {
        if (bwlog == null) return null;

        // 1. Bird mapping
        BirdReadOnlyDTO birdDTO = null;
        if (bwlog.getBird() != null) {
            birdDTO = new BirdReadOnlyDTO();
            birdDTO.setId(bwlog.getBird().getId());
            birdDTO.setName(bwlog.getBird().getName());
            birdDTO.setScientificName(bwlog.getBird().getScientificName());

            if (bwlog.getBird().getFamily() != null) {
                birdDTO.setFamily(new FamilyReadOnlyDTO(
                        bwlog.getBird().getFamily().getId(),
                        bwlog.getBird().getFamily().getName()
                ));
            }
        }

        // 2. Region mapping
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
            userDTO.setEmail(bwlog.getUser().getEmail());
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
                .createdAt(bwlog.getCreatedAt())
                .updatedAt(bwlog.getUpdatedAt())
                .build();
    }

    public void updateUserFromDto(UserInsertDTO dto, User user) {
        if (dto == null || user == null) return;

        // Update user fields (only if they're not null in DTO)
        if (dto.getFirstname() != null) {
            user.setFirstname(dto.getFirstname());
        }
        if (dto.getLastname() != null) {
            user.setLastname(dto.getLastname());
        }
        if (dto.getEmail() != null) {
            user.setEmail(dto.getEmail());
        }
        if (dto.getRole() != null) {
            user.setRole(dto.getRole());
        }
        if (dto.getIsActive() != null) {
            user.setIsActive(dto.getIsActive());
        }
        if (dto.getUsername() != null) {
            user.setUsername(dto.getUsername());
        }
        if (dto.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        // Update profile details if present
        if (dto.getProfileDetailsInsertDTO() != null) {
            if (user.getProfileDetails() == null) {
                ProfileDetails profileDetails = new ProfileDetails();
                profileDetails.setUser(user);  // Establish the bidirectional link
                user.setProfileDetails(profileDetails);
            }
            updateProfileDetailsFromDto(dto.getProfileDetailsInsertDTO(), user.getProfileDetails());
        }
    }

    private void updateProfileDetailsFromDto(ProfileDetailsInsertDTO dto, ProfileDetails profileDetails) {
        if (dto.getGender() != null) {
            profileDetails.setGender(dto.getGender());
        }
        if (dto.getDateOfBirth() != null) {
            profileDetails.setDateOfBirth(dto.getDateOfBirth());
        }
    }

    public void updateBWLFromDto(BirdwatchingLogInsertDTO dto, BirdwatchingLog log,
                                 Bird bird, Region region) {
        if (dto == null || log == null) return;

        // Update bird if provided
        if (bird != null) {
            log.setBird(bird);
        }

        // Update region if provided
        if (region != null) {
            log.setRegion(region);
        }

        // Update quantity if valid
        if (dto.getQuantity() > 0) {
            log.setQuantity(dto.getQuantity());
        }
    }

    public FamilyReadOnlyDTO mapToFamilyReadOnlyDTO(Family family) {
        return new FamilyReadOnlyDTO(
                family.getId(),
                family.getName()
        );
    }

    public RegionReadOnlyDTO mapToRegionReadOnlyDTO(Region region) {
        return new RegionReadOnlyDTO(
                region.getId(),
                region.getName()
        );
    }

    public BirdReadOnlyDTO mapToBirdReadOnlyDTO(Bird bird) {
        return new BirdReadOnlyDTO(
                bird.getId(),
                bird.getName(),
                bird.getScientificName(),
                bird.getFamily() != null ?
                        mapToFamilyReadOnlyDTO(bird.getFamily()) : null
        );
    }
}
