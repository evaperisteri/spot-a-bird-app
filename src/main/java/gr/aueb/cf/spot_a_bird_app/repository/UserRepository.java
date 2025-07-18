package gr.aueb.cf.spot_a_bird_app.repository;

import gr.aueb.cf.spot_a_bird_app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    //JpaSpecificationExecutor<User> μας δινει την find all αλλα με φιλτρα
    //JpaRepository<User, Long> μας δινει τισ CRUD μεθοδους & pagination

    Optional<User> findByProfileDetailsId(Long id);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    List<User> findByLastnameOrderByFirstnameAsc(String lastname);
    List<User> findByIsActiveTrue();

    List<User> findByUsernameContainingIgnoreCase(String partialUsername);
    List<User> findByLastnameContainingIgnoreCase(String partialLastname);

    // Find users with birdwatching logs in a region
    @Query("SELECT DISTINCT u FROM User u JOIN u.birdwatchingLogSet b WHERE b.region.id = :regionId")
    List<User> findByBirdwatchingLogsInRegion(@Param("regionId") Long regionId);

    // Find users who spotted a specific bird
    @Query("SELECT DISTINCT u FROM User u JOIN u.birdwatchingLogSet b WHERE b.bird.id = :birdId")
    List<User> findBySpottedBird(@Param("birdId") Long birdId);
}
