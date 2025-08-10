package gr.aueb.cf.spot_a_bird_app.core.specifications;

import gr.aueb.cf.spot_a_bird_app.core.filters.BirdWatchingLogFilters;
import gr.aueb.cf.spot_a_bird_app.model.Bird;
import gr.aueb.cf.spot_a_bird_app.model.BirdwatchingLog;
import gr.aueb.cf.spot_a_bird_app.model.Region;
import gr.aueb.cf.spot_a_bird_app.model.User;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class BirdwatchingLogSpecification {
    private BirdwatchingLogSpecification() {} // Prevent instantiation

    public static Specification<BirdwatchingLog> birdNameContains(String name) {
        return (root, query, cb) -> {
            if (name == null || name.isBlank())
                return cb.isTrue(cb.literal(true));
            Join<BirdwatchingLog, Bird> birdJoin = root.join("bird");
            return cb.like(cb.lower(birdJoin.get("name")), "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<BirdwatchingLog> birdScientificNameContains(String scientificName) {
        return (root, query, cb) -> {
            if (scientificName == null || scientificName.isBlank())
                return cb.isTrue(cb.literal(true));
            Join<BirdwatchingLog, Bird> birdJoin = root.join("bird");
            return cb.like(cb.lower(birdJoin.get("scientificName")), "%" + scientificName.toLowerCase() + "%");
        };
    }

    public static Specification<BirdwatchingLog> birdIdEquals(Long birdId) {
        return (root, query, cb) -> {
            if (birdId == null || birdId <= 0)
                return cb.isTrue(cb.literal(true));
            Join<BirdwatchingLog, Bird> birdJoin = root.join("bird");
            return cb.equal(birdJoin.get("id"), birdId);
        };
    }

    public static Specification<BirdwatchingLog> familyNameContains(String familyName) {
        return (root, query, cb) ->
                familyName == null ? null :
                        cb.like(
                                cb.lower(root.get("bird").get("family").get("name")),
                                "%" + familyName.toLowerCase() + "%"
                        );
    }

    public static Specification<BirdwatchingLog> familyIdEquals(Long familyId) {
        return (root, query, cb) ->
                familyId == null ? null :
                        cb.equal(
                                root.get("bird").get("family").get("id"),
        familyId
            );
    }

    public static Specification<BirdwatchingLog> regionNameContains(String regionName) {
        return (root, query, cb) -> {
            if (regionName == null || regionName.isBlank())
                return cb.isTrue(cb.literal(true));
            Join<BirdwatchingLog, Region> regionJoin = root.join("region");
            return cb.like(cb.lower(regionJoin.get("name")), "%" + regionName.toLowerCase() + "%");
        };
    }

    public static Specification<BirdwatchingLog> regionIdEquals(Long regionId) {
        return (root, query, cb) -> {
            if (regionId == null || regionId <= 0)
                return cb.isTrue(cb.literal(true));
            Join<BirdwatchingLog, Region> regionJoin = root.join("region");
            return cb.equal(regionJoin.get("id"), regionId);
        };
    }

    public static Specification<BirdwatchingLog> usernameContains(String username) {
        return (root, query, cb) -> {
            if (username == null || username.isBlank())
                return cb.isTrue(cb.literal(true));
            Join<BirdwatchingLog, User> userJoin = root.join("user");
            return cb.like(cb.lower(userJoin.get("username")), "%" + username.toLowerCase() + "%");
        };
    }

    public static Specification<BirdwatchingLog> userIdEquals(Long userId) {
        return (root, query, cb) -> {
            if (userId == null || userId <= 0)
                return cb.isTrue(cb.literal(true));
            Join<BirdwatchingLog, User> userJoin = root.join("user");
            return cb.equal(userJoin.get("id"), userId);
        };
    }

    public static Specification<BirdwatchingLog> dateEquals(LocalDateTime date) {
        return (root, query, cb) -> {
            if (date == null)
                return cb.isTrue(cb.literal(true));
            return cb.equal(
                    cb.function("date", LocalDateTime.class, root.get("observationDate")),
                    date
            );
        };
    }

}
