package gr.aueb.cf.spot_a_bird_app.core.filters;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class BirdWatchingLogFilters extends GenericFilters {

    private String name;
    private String scientificName;
    private Long birdId;


    private String regionName;
    private Long regionId;

    private String username;
    private Long userId;


    private LocalDateTime date;
}
