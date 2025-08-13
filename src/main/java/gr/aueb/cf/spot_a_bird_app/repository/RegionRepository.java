package gr.aueb.cf.spot_a_bird_app.repository;

import gr.aueb.cf.spot_a_bird_app.model.Region;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RegionRepository  extends JpaRepository<Region, Long>, JpaSpecificationExecutor<Region> {

    Optional<Region> findByName(String name);
    List<Region> findByNameContainingIgnoreCase(String namePart);
    Page<Region> findByNameContainingIgnoreCase(String namePart, Pageable pageable);

    @Query("SELECT DISTINCT r FROM Region r JOIN r.birdwatchingLogSet l WHERE l.bird.id = :birdId")
    List<Region> findRegionsWithBirdSightings(@Param("birdId") Long birdId);

    @Query(value = "SELECT COUNT(DISTINCT r.id) FROM regions r " +
            "WHERE EXISTS (SELECT 1 FROM birdwatching_logs b WHERE b.region_id = r.id)",
            nativeQuery = true)
    Long countDistinctRegionsWithLogs();
}
