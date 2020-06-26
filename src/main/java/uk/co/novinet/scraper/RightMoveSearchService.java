package uk.co.novinet.scraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import uk.co.novinet.scraper.dto.Location;
import uk.co.novinet.scraper.dto.PropertyInfo;
import uk.co.novinet.scraper.dto.SearchParameters;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class RightMoveSearchService {

    private static final float GRAVENEY_LATITUDE = 51.4233841f;
    private static final float GRAVENEY_LONGITUDE = -0.1520402f;

    private static final float TOOTING_COMMON_LATITUDE = 51.427999F;
    private static final float TOOTING_COMMON_LONGITUDE = -0.148449f;

    private static final String BASE_URL = "https://www.rightmove.co.uk";

    private static final DateTimeFormatter RIGHTMOVE_DATE_FORMAT = DateTimeFormatter.ofPattern("dd MMMM yyyy");

    private static final Pattern PATTERN_LOCATION = Pattern.compile("\\/\\/media.rightmove.co.uk\\/map\\/_generate\\?latitude=(?<latitude>[-+]?[0-9]*\\.?[0-9]+)\\&longitude=(?<longitude>[-+]?[0-9]*\\.?[0-9]+)");
    public static final String USER_AGENT = "furzedown-graveney-catchment-area-search / 0.1";

    public List<PropertyInfo> search(SearchParameters searchParameters) throws IOException {
        return findPropertyPageLinks(searchParameters).stream().map(RightMoveSearchService::findPropertyInfo).filter(propertyInfo ->
                propertyInfo.getDistanceToGraveneySchoolMeters() < searchParameters.getMaximumDistanceToGraveneySchool()).sorted((propertyInfo1, propertyInfo2) -> propertyInfo2.getDateAdded().compareTo(propertyInfo1.getDateAdded())).collect(Collectors.toList());
    }

    private static List<URI> findPropertyPageLinks(SearchParameters searchParameters) throws IOException {
        Document doc = Jsoup.connect(BASE_URL + "/property-for-sale/find.html?searchType=SALE&locationIdentifier=REGION%5E70343&insId=1&radius=0.0&minPrice=" + searchParameters.getMinimumPrice() + "&maxPrice=" + searchParameters.getMaximumPrice() + "&minBedrooms=" + searchParameters.getMinimumNumberOfBedrooms() + "&maxBedrooms=" + searchParameters.getMinimumNumberOfBedrooms() + "&displayPropertyType=houses&maxDaysSinceAdded=&_includeSSTC=on&sortByPriceDescending=&primaryDisplayPropertyType=&secondaryDisplayPropertyType=&oldDisplayPropertyType=&oldPrimaryDisplayPropertyType=&newHome=&auction=false").userAgent(USER_AGENT).get();
        return doc.select(".propertyCard-link").stream().filter(element -> !"".equals(element.attr("href").trim())).map(element -> URI.create(BASE_URL + element.attr("href"))).distinct().collect(Collectors.toList());
    }

    private static PropertyInfo findPropertyInfo(URI propertyPageUri) {
        try {
            Document propertyInfoPage = Jsoup.connect(propertyPageUri.toString()).userAgent(USER_AGENT).get();

            Location location = propertyInfoPage.select("img[alt=\"Get map and local information\"]").stream().map(element -> {
                Matcher matcher = PATTERN_LOCATION.matcher(element.attr("src"));
                if (matcher.find()) {
                    return new Location(
                            Float.parseFloat(matcher.group("latitude")),
                            Float.parseFloat(matcher.group("longitude"))
                    );
                }
                throw new IllegalArgumentException("Not found");
            }).collect(Collectors.toList()).get(0);

            return new PropertyInfo(
                    location,
                    propertyInfoPage.select("#propertyHeaderPrice strong").textNodes().get(0).toString().trim(),
                    propertyInfoPage.select(".property-header-bedroom-and-price .left address").textNodes().get(2).toString().trim().replaceAll("&amp;", "&"),
                    propertyInfoPage.select("h1.fs-22").textNodes().get(0).toString().trim(),
                    propertyPageUri.toString(),
                    distanceBetween(
                            GRAVENEY_LATITUDE,
                            GRAVENEY_LONGITUDE,
                            location.getLatitude(),
                            location.getLongitude()
                            ),
                    distanceBetween(
                            TOOTING_COMMON_LATITUDE,
                            TOOTING_COMMON_LONGITUDE,
                            location.getLatitude(),
                            location.getLongitude()
                            ),
                    LocalDate.parse(propertyInfoPage.select("#firstListedDateValue").textNodes().get(0).toString().trim(), RIGHTMOVE_DATE_FORMAT)

            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static float distanceBetween(
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
