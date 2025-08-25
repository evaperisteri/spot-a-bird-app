package gr.aueb.cf.spot_a_bird_app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BirdFullDetailsDTO {
    Long id;
    String commonName;
    String scientificName;
    String familyName;
    String imageUrl;

        // Combo box display format
        public String getDisplayText() {
            return String.format("%s (%s) | %s", commonName, scientificName, familyName);
        }

        // Search optimization
        public String getSearchableText() {
            return (commonName + " " + scientificName + " " + familyName).toLowerCase();
        }
    }
