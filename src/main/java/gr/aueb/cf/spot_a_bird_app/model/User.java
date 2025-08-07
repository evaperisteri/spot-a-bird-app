package gr.aueb.cf.spot_a_bird_app.model;

import gr.aueb.cf.spot_a_bird_app.core.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name="users")
public class User extends AbstractEntity implements UserDetails  {

    @Column(unique=true, nullable = false)
    private String username;

    private String password;

    @Column(unique=true, nullable = false)
    private String email;

    @Column
    private String firstname;

    @Column
    private String lastname;

    @Column
    @Enumerated(EnumType.STRING)
    private Role role;

    @ColumnDefault("true")
    @Column(name="is_active")
    private Boolean isActive;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name="profile_details_id")
    private ProfileDetails profileDetails;

    @OneToMany(mappedBy = "user")
    @Builder.Default    //ensures the builder keeps a default empty HashSet
    private Set<BirdwatchingLog> birdwatchingLogSet = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }
}
