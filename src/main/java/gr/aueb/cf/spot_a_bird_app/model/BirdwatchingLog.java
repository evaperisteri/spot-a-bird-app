package gr.aueb.cf.spot_a_bird_app.model;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder //πολλοί συνδυασμοί στους constructors
@Table(name="birdwatching_logs")
public class BirdwatchingLog extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="bird_id")
    private Bird bird;

    private int quantity;

    @ManyToOne
    @JoinColumn(name="region_id")
    private Region region;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;
}
