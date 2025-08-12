package gr.aueb.cf.spot_a_bird_app.repository;

import gr.aueb.cf.spot_a_bird_app.dto.stats.UserActivityDTO;
import gr.aueb.cf.spot_a_bird_app.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
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

    //Find users by username or email
    @Query("SELECT u FROM User u WHERE u.username = :username OR u.email = :email")
    Optional<User> findByUsernameOrEmail(
            @Param("username") String username,
            @Param("email") String email);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.profileDetails")
    List<User> findAllWithProfileDetails();


    @EntityGraph(attributePaths = {"profileDetails"})
    @Query("SELECT u FROM User u WHERE u.username = :username")
    Optional<User> findByUsername(@Param("username") String username);

    @Query("SELECT COUNT(u) FROM User u")
    long countTotalUsers();

    @Query("SELECT COUNT(DISTINCT l.user.id) FROM BirdwatchingLog l")
    long countActiveUsers();

    @Query("SELECT NEW gr.aueb.cf.spot_a_bird_app.dto.stats.UserActivityStatsDTO$UserActivityDTO(" +
            "u.id, u.username, COUNT(l.id), MAX(l.createdAt)) " +
            "FROM User u LEFT JOIN BirdwatchingLog l ON l.user = u " +
            "GROUP BY u.id " +
            "ORDER BY COUNT(l.id) DESC")
    List<UserActivityDTO> findMostActiveUsers(Pageable pageable);
}

