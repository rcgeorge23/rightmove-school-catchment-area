package uk.co.novinet.scraper.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchParameters {
    float maximumDistanceToGraveneySchool;
    Integer maximumPrice;
    Integer minimumPrice;
    Integer minimumNumberOfBedrooms;
    Integer maximumNumberOfBedrooms;
}
