package gr.aueb.cf.spot_a_bird_app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BirdReadOnlyDTO {
    private Long id;
    private String name;
}
