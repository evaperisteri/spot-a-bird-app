package gr.aueb.cf.spot_a_bird_app.repository;

import gr.aueb.cf.spot_a_bird_app.dto.stats.BirdCountDTO;
import gr.aueb.cf.spot_a_bird_app.dto.stats.FamilyCountDTO;
import gr.aueb.cf.spot_a_bird_app.model.Bird;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BirdRepository extends JpaRepository<Bird, Long>, JpaSpecificationExecutor<Bird> {
    Optional<Bird> findByName(String name);
    List<Bird> findByNameContainingIgnoreCase(String namePart);
    Page<Bird> findByNameContainingIgnoreCase(String name, Pageable pageable);
    @Query("SELECT b FROM Bird b JOIN b.birdwatchingLogSet s WHERE s.user.id = :userId")
    List<Bird> findBirdsSpottedByUser(@Param("userId") Long userId);

    @Query("SELECT b FROM Bird b JOIN b.birdwatchingLogSet s WHERE s.region.id = :regionId")
    List<Bird> findBirdsInRegion(@Param("regionId") Long regionId);

    List<Bird> findByFamilyId(Long familyId);

    @Query("SELECT b FROM Bird b JOIN b.family f WHERE f.name = :familyName")
    List<Bird> findByFamilyName(String familyName);

    @Query(value = "SELECT * FROM birds ORDER BY RAND() LIMIT :limit",
            nativeQuery = true)
    List<Bird> findRandomBirds(@Param("limit") int limit);

    @Query("SELECT b FROM Bird b WHERE " +
            "LOWER(b.name) LIKE LOWER(CONCAT('%',:query,'%')) OR " +
            "LOWER(b.scientificName) LIKE LOWER(CONCAT('%',:query,'%'))")
    List<Bird> findByNameOrScientificNameContaining(
            @Param("query") String query,
            Pageable pageable);

    @Query("SELECT NEW gr.aueb.cf.spot_a_bird_app.dto.stats.FamilyCountDTO(" +
            "f.id, f.name, COUNT(DISTINCT b.id), COUNT(l.id)) " +
            "FROM Bird b " +
            "JOIN b.family f " +
            "LEFT JOIN BirdwatchingLog l ON l.bird = b " +
            "GROUP BY f.id, f.name " +
            "ORDER BY COUNT(l.id) DESC")
    List<FamilyCountDTO> findTopFamiliesWithCounts(Pageable pageable);

    @Query("SELECT NEW gr.aueb.cf.spot_a_bird_app.dto.stats.BirdCountDTO(" +
            "b.id, b.name, COUNT(l)) " +
            "FROM Bird b " +
            "LEFT JOIN b.birdwatchingLogSet l " +
            "GROUP BY b.id, b.name " +
            "ORDER BY COUNT(l) DESC")
    List<BirdCountDTO> findTopBirdsByObservations(Pageable pageable);

    @Query("SELECT NEW gr.aueb.cf.spot_a_bird_app.dto.stats.BirdCountDTO(" +
            "b.id, b.name, COUNT(l)) " +
            "FROM Bird b " +
            "LEFT JOIN b.birdwatchingLogSet l " +
            "GROUP BY b.id, b.name " +
            "ORDER BY COUNT(l) DESC")
    List<BirdCountDTO> findMostSpottedBirds(Pageable pageable);

    @Query("SELECT COUNT(DISTINCT b) FROM Bird b WHERE SIZE(b.birdwatchingLogSet) > 0")
    Long countDistinctBirdsWithLogs();

    @Query("SELECT b FROM Bird b WHERE b.family.id = :familyId")
    List<Bird> findByFamilyIdWithImages(Long familyId);
}
