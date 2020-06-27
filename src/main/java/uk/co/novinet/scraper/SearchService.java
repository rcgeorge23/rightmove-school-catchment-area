package uk.co.novinet.scraper;

import uk.co.novinet.scraper.dto.PropertyInfo;
import uk.co.novinet.scraper.dto.SearchParameters;

import java.io.IOException;
import java.util.List;

public interface SearchService {

    float GRAVENEY_LATITUDE = 51.4233841f;
    float GRAVENEY_LONGITUDE = -0.1520402f;

    float TOOTING_COMMON_LATITUDE = 51.427999F;
    float TOOTING_COMMON_LONGITUDE = -0.148449f;

    String USER_AGENT = "furzedown-graveney-catchment-area-search / 0.1";

    List<PropertyInfo> search(SearchParameters searchParameters) throws IOException;

    default float distanceBetween(
            float latitude1,
            float longitude1,
            float latitude2,
            float longitude2) {
        double earthRadius = 6371000;
        double deltaLatitude = Math.toRadians(latitude2 - latitude1);
        double deltaLongitude = Math.toRadians(longitude2 - longitude1);
        double a = Math.sin(deltaLatitude / 2) * Math.sin(deltaLatitude / 2) +
                Math.cos(Math.toRadians(latitude1)) * Math.cos(Math.toRadians(latitude2)) *
                        Math.sin(deltaLongitude / 2) * Math.sin(deltaLongitude / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return (float) Math.abs(earthRadius * c);
    }
}
