package gr.aueb.cf.spot_a_bird_app.mapper;

import gr.aueb.cf.spot_a_bird_app.dto.ProfileDetailsReadOnlyDTO;
import gr.aueb.cf.spot_a_bird_app.dto.UserReadOnlyDTO;
import gr.aueb.cf.spot_a_bird_app.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Mapper {
    private final PasswordEncoder passwordEncoder;

    public UserReadOnlyDTO mapToUserReadOnlyDTO(User user){
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
}
