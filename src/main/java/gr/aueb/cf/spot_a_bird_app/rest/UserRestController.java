package gr.aueb.cf.spot_a_bird_app.rest;

import gr.aueb.cf.spot_a_bird_app.authentication.AuthenticationService;
import gr.aueb.cf.spot_a_bird_app.core.exceptions.*;
import gr.aueb.cf.spot_a_bird_app.core.filters.Paginated;
import gr.aueb.cf.spot_a_bird_app.core.filters.UserFilters;
import gr.aueb.cf.spot_a_bird_app.dto.UserInsertDTO;
import gr.aueb.cf.spot_a_bird_app.dto.UserReadOnlyDTO;
import gr.aueb.cf.spot_a_bird_app.dto.UserUpdateDTO;
import gr.aueb.cf.spot_a_bird_app.model.User;
import gr.aueb.cf.spot_a_bird_app.repository.UserRepository;
import gr.aueb.cf.spot_a_bird_app.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;
    private final AuthenticationService authService;
    private final UserRepository userRepository;

    @PostMapping("/users/save")
    public ResponseEntity<UserReadOnlyDTO> saveUser(@Valid @RequestBody UserInsertDTO userInsertDTO,
           BindingResult bindingResult)
            throws AppObjectInvalidArgumentException, ValidationException, AppObjectAlreadyExists, AppServerException, IOException {
        if(bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }
            UserReadOnlyDTO userReadOnlyDTO = userService.saveUser(userInsertDTO);
            return new ResponseEntity<>(userReadOnlyDTO, HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserReadOnlyDTO> getUserById(@PathVariable Long id)
            throws AppObjectNotFoundException {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserReadOnlyDTO>> getAllUsers()
            throws AppObjectNotAuthorizedException {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/users/paginated")
    public ResponseEntity<Page<UserReadOnlyDTO>> getPaginatedUsers(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        Page<UserReadOnlyDTO> usersPage = userService.getPaginatedUsers(page, size);
        return new ResponseEntity<>(usersPage, HttpStatus.OK);
    }

    @PostMapping("/users/filtered")
    public ResponseEntity<List<UserReadOnlyDTO>> getFilteredUsers(@Nullable @RequestBody UserFilters filters) throws AppObjectNotAuthorizedException {
        if (filters==null) filters = UserFilters.builder().build();
        return ResponseEntity.ok(userService.getUsersFiltered(filters));
    }

    @PostMapping("/users/filtered/paginated")
    public ResponseEntity<Paginated<UserReadOnlyDTO>> getFilteredPaginatedUsers
            (@RequestBody(required = false) UserFilters filters) throws AppObjectNotAuthorizedException {
        filters = filters != null ? filters : new UserFilters();
        return ResponseEntity.ok(userService.getUsersFilteredPaginated(filters));
    }

    @GetMapping("/my-info")
    public ResponseEntity<UserReadOnlyDTO> getCurrentUserInfo() throws AppObjectNotFoundException {
        return ResponseEntity.ok(userService.getCurrentUserInfo());
    }

    @PutMapping("/update-user")
    public ResponseEntity<UserReadOnlyDTO> updateCurrentUser(
            @Valid @RequestBody UserUpdateDTO userUpdateDTO,
            BindingResult bindingResult)
            throws AppObjectInvalidArgumentException, ValidationException, AppObjectNotFoundException, AppServerException, AppObjectAlreadyExists {

        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        // Get the current user's ID from authentication
        String username = authService.getAuthenticatedUsername();
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppObjectNotFoundException("User", "User not found"));

        return ResponseEntity.ok(userService.updateUser(currentUser.getId(), userUpdateDTO));
    }


    @PutMapping("/users/{id}")
    public ResponseEntity<UserReadOnlyDTO> updateUser(@PathVariable Long id,
                                                      @Valid @RequestBody UserUpdateDTO userUpdateDTO,
                                                      BindingResult bindingResult)
            throws AppObjectInvalidArgumentException, ValidationException, AppObjectNotFoundException, AppServerException, AppObjectAlreadyExists {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        return ResponseEntity.ok(userService.updateUser(id, userUpdateDTO));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) throws AppObjectNotFoundException {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/my-info/delete")
    public ResponseEntity<String> deleteCurrentUser() throws AppObjectNotFoundException {
        String username = authService.getAuthenticatedUsername();
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppObjectNotFoundException("User", "User not found"));

        userService.deleteUser(currentUser.getId());
        return ResponseEntity.ok("User account deleted successfully");
    }

}
