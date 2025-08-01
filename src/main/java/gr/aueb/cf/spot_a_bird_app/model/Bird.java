package gr.aueb.cf.spot_a_bird_app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name="birds")
public class Bird {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    @Column
    private String scientificName;

    @OneToMany(mappedBy = "bird")
    private Set<BirdwatchingLog> birdwatchingLogSet = new HashSet<>();
}
