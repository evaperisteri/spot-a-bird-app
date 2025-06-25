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

public interface BirdwatchingLogRepository extends JpaRepository<BirdwatchingLog, Long>, JpaSpecificationExecutor<BirdwatchingLog> {

    List<BirdwatchingLog> findByUser(User user);

    Page<BirdwatchingLog> findByUser(User user, Pageable pageable);

    //retrieve all birdwatching logs made by a specific user in a specific region
    @Query("SELECT b FROM BirdwatchingLog b WHERE b.user.id = :userId AND b.region.id = :regionId")
    List<BirdwatchingLog> findByUserAndRegion(@Param("userId") Long userId, @Param("regionId") Long regionId);

    @Query("SELECT COUNT(b) FROM BirdwatchingLog b WHERE b.user.id = :userId")
    Long countByUser(@Param("userId") Long userId);

    @Query("SELECT b.bird.name, COUNT(b) FROM BirdwatchingLog b WHERE b.user.id = :userId GROUP BY b.bird.name")
    List<Object[]> countSightingsPerBirdByUser(@Param("userId") Long userId);
}
