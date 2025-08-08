package gr.aueb.cf.spot_a_bird_app.service;

import gr.aueb.cf.spot_a_bird_app.core.exceptions.AppObjectAlreadyExists;
import gr.aueb.cf.spot_a_bird_app.core.exceptions.AppObjectInvalidArgumentException;
import gr.aueb.cf.spot_a_bird_app.core.exceptions.AppObjectNotAuthorizedException;
import gr.aueb.cf.spot_a_bird_app.core.exceptions.AppObjectNotFoundException;
import gr.aueb.cf.spot_a_bird_app.core.filters.Paginated;
import gr.aueb.cf.spot_a_bird_app.core.filters.UserFilters;
import gr.aueb.cf.spot_a_bird_app.core.specifications.UserSpecification;
import gr.aueb.cf.spot_a_bird_app.dto.UserInsertDTO;
import gr.aueb.cf.spot_a_bird_app.dto.UserReadOnlyDTO;
import gr.aueb.cf.spot_a_bird_app.mapper.Mapper;
import gr.aueb.cf.spot_a_bird_app.model.ProfileDetails;
import gr.aueb.cf.spot_a_bird_app.model.User;
import gr.aueb.cf.spot_a_bird_app.repository.ProfileDetailsRepository;
import gr.aueb.cf.spot_a_bird_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final static Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final Mapper mapper;
    private final ProfileDetailsRepository profileDetailsRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(rollbackFor = {AppObjectAlreadyExists.class, IOException.class})
    public UserReadOnlyDTO saveUser(UserInsertDTO userInsertDTO) throws AppObjectAlreadyExists, AppObjectInvalidArgumentException, IOException {
        // 1. Validate input
        if (userInsertDTO == null) {
            throw new AppObjectInvalidArgumentException("User", "User data cannot be null");
        }

        if (userInsertDTO.getPassword() == null || userInsertDTO.getPassword().trim().isEmpty()) {
            throw new AppObjectInvalidArgumentException("User", "Password cannot be null or empty");
        }

        // 2. Check for existing users (single query optimization)
        Optional<User> existingUser = userRepository.findByUsernameOrEmail(
                userInsertDTO.getUsername(),
                userInsertDTO.getEmail()
        );

        if (existingUser.isPresent()) {
            User user = existingUser.get();
            if (user.getUsername().equals(userInsertDTO.getUsername())) {
                throw new AppObjectAlreadyExists("User", "Username already exists");
            } else {
                throw new AppObjectAlreadyExists("User", "Email already exists");
            }
        }

        // 3. Create and save new user

        User user = mapper.mapToUser(userInsertDTO);
        user.setPassword(passwordEncoder.encode(userInsertDTO.getPassword()));

        if (userInsertDTO.getProfileDetailsInsertDTO() != null) {
            ProfileDetails profile = new ProfileDetails();
            profile.setGender(userInsertDTO.getProfileDetailsInsertDTO().getGender());
            profile.setDateOfBirth(userInsertDTO.getProfileDetailsInsertDTO().getDateOfBirth());

            profile.setUser(user);  // This links ProfileDetails to User
            user.setProfileDetails(profile);  // This links User to ProfileDetails
        }
        User savedUser = userRepository.save(user); //this one has an id

        return mapper.mapToUserReadOnlyDTO(savedUser);
    }

    @Transactional(readOnly = true)
    public UserReadOnlyDTO getUserById(Long id) throws AppObjectNotFoundException {
        return userRepository.findById(id)
                .map(mapper::mapToUserReadOnlyDTO)
                .orElseThrow(() -> new AppObjectNotFoundException("User", "User not found with id: " + id));
    }

    @Transactional
    public UserReadOnlyDTO updateUser(Long id, UserInsertDTO updateDTO)
            throws AppObjectNotFoundException, AppObjectAlreadyExists {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new AppObjectNotFoundException("User", "User not found with id: " + id));

        // Check for duplicate username/email if changing those fields
        if (updateDTO.getUsername() != null && !updateDTO.getUsername().equals(existingUser.getUsername())) {
            if (userRepository.findByUsername(updateDTO.getUsername()).isPresent()) {
                throw new AppObjectAlreadyExists("User", "Username already exists");
            }
            existingUser.setUsername(updateDTO.getUsername());
        }

        if (updateDTO.getEmail() != null && !updateDTO.getEmail().equals(existingUser.getEmail())) {
            if (userRepository.findByEmail(updateDTO.getEmail()).isPresent()) {
                throw new AppObjectAlreadyExists("User", "Email already exists");
            }
            existingUser.setEmail(updateDTO.getEmail());
        }

        // Update other fields
        mapper.updateUserFromDto(updateDTO, existingUser);
        User updatedUser = userRepository.save(existingUser);
        return mapper.mapToUserReadOnlyDTO(updatedUser);
    }

    @Transactional
    public void deleteUser(Long id) throws AppObjectNotFoundException {
        if (!userRepository.existsById(id)) {
            throw new AppObjectNotFoundException("User", "User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    public List<UserReadOnlyDTO> getAllUsers() throws AppObjectNotAuthorizedException {

        return userRepository.findAllWithProfileDetails().stream()
                .map(mapper::mapToUserReadOnlyDTO)
                .toList();
    }

    //fixed sorting
    @Transactional
    public Page<UserReadOnlyDTO> getPaginatedUsers(int page, int size) {
        String defaultSort = "id";
        Pageable pageable = PageRequest.of(page, size, Sort.by(defaultSort).ascending());
        return userRepository.findAll(pageable).map(mapper::mapToUserReadOnlyDTO);
    }

    //dynamic sorting
    @Transactional
    public Page<UserReadOnlyDTO> getUsersPaginatedAndSorted(int page, int size, String sortBy, String sortDirection){
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return userRepository.findAll(pageable).map(mapper::mapToUserReadOnlyDTO);
    }

    @Transactional
    public List<UserReadOnlyDTO> getUsersFiltered (UserFilters filters) {
        return userRepository.findAll(getSpecsFromFilters(filters))
                .stream()
                .map(mapper::mapToUserReadOnlyDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public Paginated<UserReadOnlyDTO> getUsersFilteredPaginated(UserFilters filters) {
        var filtered = userRepository.findAll(getSpecsFromFilters(filters), filters.getPageable());
        return new Paginated<> (filtered.map(mapper::mapToUserReadOnlyDTO));
    }
    //integrated specifications
    private Specification<User> getSpecsFromFilters(UserFilters userFilters) {
        return Specification
                .where(UserSpecification.userIds(userFilters.getId()))
                .and(UserSpecification.userGenderIs(userFilters.getGender()))
                .and(UserSpecification.userDateOfBirthIs(userFilters.getDateOfBirth()));
    }
}
