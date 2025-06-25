package gr.aueb.cf.spot_a_bird_app.repository;

import gr.aueb.cf.spot_a_bird_app.model.Bird;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BirdRepository extends JpaRepository<Bird, Long>, JpaSpecificationExecutor<Bird> {
    Optional<Bird> findByName(String name);
    List<Bird> findByNameContainingIgnoreCase(String namePart);
    @Query("SELECT b FROM Bird b JOIN b.birdwatchingLogSet s WHERE s.user.id = :userId")
    List<Bird> findBirdsSpottedByUser(@Param("userId") Long userId);

    @Query("SELECT b FROM Bird b JOIN b.birdwatchingLogSet s WHERE s.region.id = :regionId")
    List<Bird> findBirdsInRegion(@Param("regionId") Long regionId);
}
