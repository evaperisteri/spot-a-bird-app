package gr.aueb.cf.spot_a_bird_app.service;

import gr.aueb.cf.spot_a_bird_app.core.exceptions.AppObjectAlreadyExists;
import gr.aueb.cf.spot_a_bird_app.core.exceptions.AppObjectInvalidArgumentException;
import gr.aueb.cf.spot_a_bird_app.core.filters.Paginated;
import gr.aueb.cf.spot_a_bird_app.core.filters.UserFilters;
import gr.aueb.cf.spot_a_bird_app.core.specifications.UserSpecification;
import gr.aueb.cf.spot_a_bird_app.dto.UserInsertDTO;
import gr.aueb.cf.spot_a_bird_app.dto.UserReadOnlyDTO;
import gr.aueb.cf.spot_a_bird_app.mapper.Mapper;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

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

    //fixed sorting
    @Transactional
    public Page<UserReadOnlyDTO> getPaginatedUsers(int page, int size) {
        String defaultSort = "id";
        Pageable pageable = PageRequest.of(page, size, Sort.by(defaultSort).ascending());
        return userRepository.findAll(pageable).map(mapper::mapToUserReadOnlyDTO);
    }

    //dynamic sorting
    @Transactional
    public Page<UserReadOnlyDTO> getPaginatedAndSortedUsers(int page, int size, String sortBy, String sortDirection){
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return userRepository.findAll(pageable).map(mapper::mapToUserReadOnlyDTO);
    }

    public List<UserReadOnlyDTO> getUsersFiltered (UserFilters filters) {
        return userRepository.findAll(getSpecsFromFilters(filters)).stream().map(mapper::mapToUserReadOnlyDTO).collect(Collectors.toList());
    }

    public Paginated<UserReadOnlyDTO> getUsersFilteredPaginated(UserFilters filters) {
        var filtered = userRepository.findAll(getSpecsFromFilters(filters), filters.getPageable());
        return new Paginated<> (filtered.map(mapper::mapToUserReadOnlyDTO));
    }
    //intergrated specifications
    private Specification<User> getSpecsFromFilters(UserFilters userFilters) {
        return Specification
                .where(UserSpecification.userIdIs(userFilters.getId()))
                .and(UserSpecification.userGenderIs(userFilters.getGender()))
                .and(UserSpecification.userDateOfBirthIs(userFilters.getDateOfBirth()));
    }
}
