package gr.aueb.cf.spot_a_bird_app.rest;

import gr.aueb.cf.spot_a_bird_app.core.exceptions.*;
import gr.aueb.cf.spot_a_bird_app.core.filters.Paginated;
import gr.aueb.cf.spot_a_bird_app.core.filters.UserFilters;
import gr.aueb.cf.spot_a_bird_app.dto.UserInsertDTO;
import gr.aueb.cf.spot_a_bird_app.dto.UserReadOnlyDTO;
import gr.aueb.cf.spot_a_bird_app.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;
    @PostMapping("/users/save")
    public ResponseEntity<UserReadOnlyDTO> saveUser(
            @Valid @RequestPart(name = "user")UserInsertDTO userInsertDTO, BindingResult bindingResult)
            throws AppObjectInvalidArgumentException, ValidationException, AppObjectAlreadyExists, AppServerException, IOException {
        if(bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }
            UserReadOnlyDTO userReadOnlyDTO = userService.saveUser(userInsertDTO);
            return new ResponseEntity<>(userReadOnlyDTO, HttpStatus.OK);

    }

    @GetMapping("/users/paginated")
    public ResponseEntity<Page<UserReadOnlyDTO>> getPaginatedUsers(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        Page<UserReadOnlyDTO> usersPage = userService.getPaginatedUsers(page, size);
        return new ResponseEntity<>(usersPage, HttpStatus.OK);
    }

    @GetMapping("/users/filtered")
    public ResponseEntity<List<UserReadOnlyDTO>> getFilteredUsers(@Nullable @RequestBody UserFilters filters) throws AppObjectNotAuthorizedException {
        if (filters==null) UserFilters.builder().build();
        return ResponseEntity.ok(userService.getUsersFiltered(filters));
    }

    public ResponseEntity<Paginated<UserReadOnlyDTO>> getUsersFilteredPaginated (@RequestBody UserFilters filters) {

    }
}
