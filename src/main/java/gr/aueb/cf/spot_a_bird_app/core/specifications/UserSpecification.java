package gr.aueb.cf.spot_a_bird_app.core.specifications;

import gr.aueb.cf.spot_a_bird_app.core.enums.Gender;
import gr.aueb.cf.spot_a_bird_app.model.ProfileDetails;
import gr.aueb.cf.spot_a_bird_app.model.User;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class UserSpecification {
    //private constructor για να μην μπορεί να γίνει instantiate, only public static methods
    private UserSpecification(){}
    public static Specification<User> userGenderIs(Gender gender) {
        return((root, query, criteriaBuilder)->{
            if(gender == null) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            Join<User, ProfileDetails> userProfile = root.join("profileDetails");
            return criteriaBuilder.equal(userProfile.get("gender"), gender);
        });
    }

    public static Specification<User> userDateOfBirthIs(LocalDate dateOfBirth) {
        return((root, query, criteriaBuilder)->{
            if(dateOfBirth == null) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            Join<User, ProfileDetails> userProfile = root.join("profileDetails");
            return criteriaBuilder.equal(userProfile.get("dateOfBirth"), dateOfBirth);
        });
    }

    //value.trim().isEmpty() = value.isBlank()
    public static Specification<User> userStringFieldLike(String field, String value) {
        return((root, query, criteriaBuilder)->{
            if(value == null || value.trim().isEmpty()) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            return criteriaBuilder.like(criteriaBuilder.upper(root.get(field)), "%" + value.toUpperCase() + "%");
        });
    }
}
