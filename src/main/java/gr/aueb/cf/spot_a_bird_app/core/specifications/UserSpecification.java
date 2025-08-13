package gr.aueb.cf.spot_a_bird_app.core.specifications;

import gr.aueb.cf.spot_a_bird_app.core.enums.Gender;
import gr.aueb.cf.spot_a_bird_app.core.enums.Role;
import gr.aueb.cf.spot_a_bird_app.model.ProfileDetails;
import gr.aueb.cf.spot_a_bird_app.model.User;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class UserSpecification {
    //private constructor για να μην μπορεί να γίνει instantiate, only public static methods
    private UserSpecification(){}

    public static Specification<User> userIdIs(Long id) {
        return (root, query, cb) ->
                id == null ? cb.conjunction() : cb.equal(root.get("id"), id);
    }

    public static Specification<User> usernameContains(String username) {
        return (root, query, cb) ->
                username == null || username.isBlank() ?
                        cb.conjunction() :
                        cb.like(cb.lower(root.get("username")), "%" + username.toLowerCase() + "%");
    }

    public static Specification<User> emailContains(String email) {
        return (root, query, cb) ->
                email == null || email.isBlank() ?
                        cb.conjunction() :
                        cb.like(cb.lower(root.get("email")), "%" + email.toLowerCase() + "%");
    }

    public static Specification<User> roleIs(Role role) {
        return (root, query, cb) ->
                role == null ? cb.conjunction() : cb.equal(root.get("role"), role);
    }

    public static Specification<User> isActive(Boolean isActive) {
        return (root, query, cb) ->
                isActive == null ? cb.conjunction() : cb.equal(root.get("isActive"), isActive);
    }

    public static Specification<User> genderIs(Gender gender) {
        return (root, query, cb) -> {
            if (gender == null) return cb.conjunction();
            Join<User, ProfileDetails> profileJoin = root.join("profileDetails");
            return cb.equal(profileJoin.get("gender"), gender);
        };
    }

    public static Specification<User> userDateOfBirthIs(LocalDate dateOfBirth) {
        return (root, query, cb) -> {
            if (dateOfBirth == null) return cb.conjunction();
            Join<User, ProfileDetails> profileJoin = root.join("profileDetails");
            return cb.equal(profileJoin.get("dateOfBirth"), dateOfBirth);
        };
    }

    //value.trim().isEmpty() = value.isBlank()
    public static Specification<User> userProfileDetailsIdIs(Long profileDetailsId) {
        return((root, query, criteriaBuilder)->{
            if(profileDetailsId == null || profileDetailsId < 0) return criteriaBuilder.conjunction();
            Join<User, ProfileDetails> userProfile = root.join("profileDetails");
            return criteriaBuilder.equal(userProfile.get("id"), profileDetailsId);
        });
    }
}
