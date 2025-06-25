package gr.aueb.cf.spot_a_bird_app.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponseDTO {
    private String firstname;
    private String lastname;
    private String token;
}
