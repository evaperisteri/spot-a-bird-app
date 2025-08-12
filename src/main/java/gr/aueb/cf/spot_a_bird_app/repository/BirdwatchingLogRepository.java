package gr.aueb.cf.spot_a_bird_app.repository;

import gr.aueb.cf.spot_a_bird_app.model.BirdwatchingLog;
import gr.aueb.cf.spot_a_bird_app.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BirdwatchingLogRepository extends JpaRepository<BirdwatchingLog, Long>, JpaSpecificationExecutor<BirdwatchingLog> {

    List<BirdwatchingLog> findByUser(User user);

    Page<BirdwatchingLog> findByUser(User user, Pageable pageable);

    @Query("SELECT b FROM BirdwatchingLog b " +
            "LEFT JOIN FETCH b.bird " +
            "LEFT JOIN FETCH b.bird.family " +  // Ensure Bird's family is loaded
            "LEFT JOIN FETCH b.user " +
            "LEFT JOIN FETCH b.user.profileDetails " +  // Ensure User's profile is loaded
            "WHERE b.id = :id")
    Optional<BirdwatchingLog> findByIdWithDetails(@Param("id") Long id);

    //retrieve all birdwatching logs made by a specific user in a specific region
    @Query("SELECT b FROM BirdwatchingLog b WHERE b.user.id = :userId AND b.region.id = :regionId")
    List<BirdwatchingLog> findByUserAndRegion(@Param("userId") Long userId, @Param("regionId") Long regionId);

    @Query("SELECT COUNT(b) FROM BirdwatchingLog b WHERE b.user.id = :userId")
    Long countByUser(@Param("userId") Long userId);

    @Query("SELECT b.bird.name, COUNT(b) FROM BirdwatchingLog b WHERE b.user.id = :userId GROUP BY b.bird.name")
    List<Object[]> countSightingsPerBirdByUser(@Param("userId") Long userId);

    @Query("SELECT l FROM BirdwatchingLog l JOIN l.bird b WHERE (:birdName IS NULL OR LOWER(b.name) LIKE LOWER(CONCAT('%', :birdName, '%')) AND l.user.id = :userId")
    List<BirdwatchingLog> findFilteredLogs(
            @Param("birdName") String birdName,
            @Param("userId") Long userId);
}
