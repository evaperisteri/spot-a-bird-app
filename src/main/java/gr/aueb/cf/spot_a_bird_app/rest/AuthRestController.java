package gr.aueb.cf.spot_a_bird_app.rest;

import gr.aueb.cf.spot_a_bird_app.authentication.AuthenticationService;
import gr.aueb.cf.spot_a_bird_app.core.exceptions.AppObjectNotAuthorizedException;
import gr.aueb.cf.spot_a_bird_app.dto.AuthenticationRequestDTO;
import gr.aueb.cf.spot_a_bird_app.dto.AuthenticationResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthRestController {
    private final AuthenticationService authService;

    public AuthRestController(AuthenticationService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDTO> login(@RequestBody AuthenticationRequestDTO request) throws AppObjectNotAuthorizedException {
        return ResponseEntity.ok(authService.authenticate(request));
    }
}
