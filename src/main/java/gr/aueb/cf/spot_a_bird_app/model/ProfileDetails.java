package gr.aueb.cf.spot_a_bird_app.model;

import gr.aueb.cf.spot_a_bird_app.core.enums.Gender;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name="profile_details")
public class ProfileDetails extends AbstractEntity {

    @Column(name="date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @OneToOne(mappedBy= "profileDetails")
    private User user;
    }
