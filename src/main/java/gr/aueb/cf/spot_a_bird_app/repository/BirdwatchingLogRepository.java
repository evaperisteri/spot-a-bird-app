package gr.aueb.cf.spot_a_bird_app.repository;

import gr.aueb.cf.spot_a_bird_app.dto.stats.BirdCountDTO;
import gr.aueb.cf.spot_a_bird_app.dto.stats.RegionCountDTO;
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
            "LEFT JOIN FETCH b.bird.family " +
            "LEFT JOIN FETCH b.user " +
            "LEFT JOIN FETCH b.user.profileDetails " +
            "WHERE b.id = :id")
    Optional<BirdwatchingLog> findByIdWithDetails(@Param("id") Long id);

    //retrieve all birdwatching logs made by a specific user in a specific region
    @Query("SELECT b FROM BirdwatchingLog b WHERE b.user.id = :userId AND b.region.id = :regionId")
    List<BirdwatchingLog> findByUserAndRegion(@Param("userId") Long userId, @Param("regionId") Long regionId);

    @Query("SELECT COUNT(b) FROM BirdwatchingLog b WHERE b.user.id = :userId")
    Long countByUser(@Param("userId") Long userId);

    @Query("SELECT b.bird.name, COUNT(b) FROM BirdwatchingLog b WHERE b.user.id = :userId GROUP BY b.bird.name")
    List<Object[]> countSightingsPerBirdByUser(@Param("userId") Long userId);

    @Query("SELECT l FROM BirdwatchingLog l " +
            "JOIN l.bird b " +
            "WHERE (:birdName IS NULL OR LOWER(b.name) LIKE LOWER(CONCAT('%', :birdName, '%'))) " +
            "AND l.user.id = :userId")
    List<BirdwatchingLog> findFilteredLogs(
            @Param("birdName") String birdName,
            @Param("userId") Long userId);

    @Query("SELECT b.id as birdId, b.name as birdName, COUNT(l.id) as observationCount " +
            "FROM BirdwatchingLog l " +
            "JOIN l.bird b " +
            "WHERE l.user.username = :username " +
            "GROUP BY b.id " +
            "ORDER BY observationCount DESC")
    List<BirdCountDTO> findTopBirdsByUser(String username, Pageable pageable);

    @Query("SELECT NEW gr.aueb.cf.spot_a_bird_app.dto.stats.RegionCountDTO(" +
            "r.id, r.name, COUNT(l.id)) " +
            "FROM BirdwatchingLog l " +
            "JOIN l.region r " +
            "GROUP BY r.id " +
            "ORDER BY COUNT(l.id) DESC")
    List<RegionCountDTO> findTopRegionsByObservations(Pageable pageable);

    // Count all logs for a specific user
    long countByUserUsername(String username);

    // Count distinct birds observed by a user
    @Query("SELECT COUNT(DISTINCT l.bird.id) FROM BirdwatchingLog l WHERE l.user.username = :username")
    long countDistinctBirdsByUser(@Param("username") String username);

    // Count distinct regions visited by a user
    @Query("SELECT COUNT(DISTINCT l.region.id) FROM BirdwatchingLog l WHERE l.user.username = :username")
    long countDistinctRegionsByUser(@Param("username") String username);
}
