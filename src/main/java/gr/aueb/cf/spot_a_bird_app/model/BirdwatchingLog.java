package gr.aueb.cf.spot_a_bird_app.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name="birdwatching_logs")
public class BirdwatchingLog extends AbstractEntity {

    @Column(nullable = false)
    private int quantity;

    @ManyToOne
    @JoinColumn(name="region_id", nullable = false)
    private Region region;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "bird_id")
    private Bird bird;
}
