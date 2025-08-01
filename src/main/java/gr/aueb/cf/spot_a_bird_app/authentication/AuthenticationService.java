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

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));

        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(
                ()->new AppObjectNotAuthorizedException("User", "Not an authorized user.")
        );

        String token = jwtService.generateToken(authentication.getName(),user.getRole().name());
        return new AuthenticationResponseDTO(user.getFirstname(), user.getLastname(), token);

    }

    public String getAuthenticatedUsername() throws AppObjectNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AppObjectNotFoundException("User", "No authenticated user found");
        }
        return authentication.getName(); // Works because JwtAuthenticationToken.getName() returns the subject
    }
}
