package gr.aueb.cf.spot_a_bird_app.repository;

import gr.aueb.cf.spot_a_bird_app.core.enums.Gender;
import gr.aueb.cf.spot_a_bird_app.model.ProfileDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProfileDetailsRepository extends JpaRepository<ProfileDetails, Long>, JpaSpecificationExecutor<ProfileDetails> {
    Optional<ProfileDetails> findByUserId(Long userId);
    List<ProfileDetails> findByGender(Gender gender);
    Page<ProfileDetails> findByGender(Gender gender, Pageable pageable);
    // profiles born between dates
    List<ProfileDetails> findByDateOfBirthBetween(LocalDate start, LocalDate end);

    @Query("SELECT pd.gender, COUNT(pd) FROM ProfileDetails pd GROUP BY pd.gender")
    List<Object[]> countProfilesByGender();

    @Modifying
    @Transactional
    @Query("UPDATE ProfileDetails pd SET pd.dateOfBirth = :dob WHERE pd.user.id = :userId")
    void updateBirthDate(@Param("userId") Long userId, @Param("dob") LocalDate dateOfBirth);
}
