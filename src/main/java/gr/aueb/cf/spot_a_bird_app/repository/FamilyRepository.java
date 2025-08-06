package gr.aueb.cf.spot_a_bird_app.repository;

import gr.aueb.cf.spot_a_bird_app.model.Family;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface FamilyRepository extends JpaRepository<Family, Long>, JpaSpecificationExecutor<Family> {

    Optional<Family> findByName(String name);
}
