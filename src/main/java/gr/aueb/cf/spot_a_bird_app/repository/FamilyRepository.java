package gr.aueb.cf.spot_a_bird_app.repository;

import gr.aueb.cf.spot_a_bird_app.model.Family;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FamilyRepository extends JpaRepository<Family, Long>, JpaSpecificationExecutor<Family> {

    Optional<Family> findByName(String name);
    List<Family> findByNameContainingIgnoreCase(String name);
    @Query("SELECT f FROM Family f JOIN f.birds b WHERE b.id = :birdId")
    Optional<Family> findByBirdId(@Param("birdId") Long birdId);
}
