package gr.aueb.cf.spot_a_bird_app.authentication;

import gr.aueb.cf.spot_a_bird_app.core.exceptions.AppObjectNotAuthorizedException;
import gr.aueb.cf.spot_a_bird_app.core.exceptions.AppObjectNotFoundException;
import gr.aueb.cf.spot_a_bird_app.dto.AuthenticationRequestDTO;
import gr.aueb.cf.spot_a_bird_app.dto.AuthenticationResponseDTO;
import gr.aueb.cf.spot_a_bird_app.model.User;
import gr.aueb.cf.spot_a_bird_app.repository.UserRepository;
import gr.aueb.cf.spot_a_bird_app.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO dto) throws AppObjectNotAuthorizedException {
        // 1. Authenticate user credentials
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));
        // 2. Get full user details from database
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(
                ()->new AppObjectNotAuthorizedException("User", "Not an authorized user.")
        );
        // 3. Generate JWT with all required claims
        String token = jwtService.generateToken(
                user.getId(),
                user.getUsername(),
                user.getRole().name()
        );

        // 4. Return response with user details and token
        return AuthenticationResponseDTO.builder()
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .token(token)
                .userId(user.getId())
                .username(user.getUsername())
                .role(user.getRole().name())
                .build();
    }

    public String getAuthenticatedUsername() throws AppObjectNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {

            throw new AppObjectNotFoundException("User", "No authenticated user found");
        }
        return authentication.getName(); //returns the subject
    }

    public Long getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new SecurityException("No authenticated user found");
        }
        return ((CustomUserDetails) authentication.getPrincipal()).getUser().getId();
    }
}
