package gr.aueb.cf.spot_a_bird_app.service;

import gr.aueb.cf.spot_a_bird_app.core.exceptions.AppObjectAlreadyExists;
import gr.aueb.cf.spot_a_bird_app.core.exceptions.AppObjectInvalidArgumentException;
import gr.aueb.cf.spot_a_bird_app.dto.UserInsertDTO;
import gr.aueb.cf.spot_a_bird_app.dto.UserReadOnlyDTO;
import gr.aueb.cf.spot_a_bird_app.mapper.Mapper;
import gr.aueb.cf.spot_a_bird_app.model.User;
import gr.aueb.cf.spot_a_bird_app.repository.ProfileDetailsRepository;
import gr.aueb.cf.spot_a_bird_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class UserService {
    private final static Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final Mapper mapper;
    private final ProfileDetailsRepository profileDetailsRepository;

    @Transactional(rollbackFor = {AppObjectAlreadyExists.class, IOException.class})
    public UserReadOnlyDTO saveUser(UserInsertDTO userInsertDTO) throws AppObjectAlreadyExists, AppObjectInvalidArgumentException, IOException {
        if (userRepository.findByUsername(userInsertDTO.getUsername()).isPresent()) {
            throw new AppObjectAlreadyExists("User", "user with username " + userInsertDTO.getUsername() + " already exists.");
        }

        if (userRepository.findByEmail(userInsertDTO.getEmail()).isPresent()) {
            throw new AppObjectAlreadyExists("User", "user with email " + userInsertDTO.getEmail() + " already exists.");
        }

        User user = mapper.mapToUser(userInsertDTO);
        User savedUser = userRepository.save(user); //this one has an id

        return mapper.mapToUserReadOnlyDTO(savedUser);
    }
}
