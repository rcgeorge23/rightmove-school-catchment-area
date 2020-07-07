package uk.co.novinet.scraper;

import org.apache.commons.lang3.ObjectUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
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

import static org.apache.commons.lang3.ObjectUtils.compare;
import static uk.co.novinet.scraper.rest.HomeController.SCHOOLS;

@Service
public class ZooplaSearchService implements SearchService {

    private static final String BASE_URL = "https://www.zoopla.co.uk";

    private static final DateTimeFormatter ZOOPLA_DATE_FORMAT = DateTimeFormatter.ofPattern("d MMM yyyy");

    private static final Pattern PATTERN_LOCATION = Pattern.compile("\"coordinates\": \\{\"is_approximate\":false,\"latitude\":(?<latitude>[-+]?[0-9]*\\.?[0-9]+),\"longitude\":(?<longitude>[-+]?[0-9]*\\.?[0-9]+)\\}");

    public List<PropertyInfo> search(SearchParameters searchParameters) throws IOException {
        return findPropertyPageLinks(searchParameters).stream().map((URI propertyPageUri) ->
                findPropertyInfo(propertyPageUri, searchParameters)).filter(propertyInfo ->
                    propertyInfo.getDistanceToSchoolMeters() < searchParameters.getMaximumDistanceToSchool())
                .sorted((propertyInfo1, propertyInfo2) ->
                        compare(propertyInfo2.getDateAdded(), propertyInfo1.getDateAdded()))
                .collect(Collectors.toList());
    }

    private static List<URI> findPropertyPageLinks(SearchParameters searchParameters) throws IOException {
        String url = BASE_URL + "/for-sale/houses/furzedown/?beds_min=" + searchParameters.getMinimumNumberOfBedrooms() + "&is_retirement_home=false&is_shared_ownership=false&new_homes=exclude&price_max=" + searchParameters.getMaximumPrice() + "&price_min=" + searchParameters.getMinimumPrice() + "&q=Furzedown%2C%20London&results_sort=newest_listings&search_source=home&page_size=50";
        Document doc = Jsoup.connect(url).userAgent(USER_AGENT).get();
        List<URI> uris = doc.select("h2.listing-results-attr a").stream().filter(element -> !"".equals(element.attr("href").trim())).map(element -> URI.create(BASE_URL + element.attr("href"))).distinct().collect(Collectors.toList());
        return uris;
    }

    private PropertyInfo findPropertyInfo(URI propertyPageUri, SearchParameters searchParameters) {
        try {
            Document propertyInfoPage = Jsoup.connect(propertyPageUri.toString()).userAgent(USER_AGENT).get();

            Elements scriptElements = propertyInfoPage.select("script");

            Location location = scriptElements.stream().filter(element -> PATTERN_LOCATION.matcher(element.outerHtml()).find()).map(element -> {
                Matcher matcher = PATTERN_LOCATION.matcher(element.outerHtml());
                if (matcher.find()) {
                    return new Location(
                            Float.parseFloat(matcher.group("latitude")),
                            Float.parseFloat(matcher.group("longitude"))
                    );
                }

                throw new IllegalArgumentException("Not found");
            }).collect(Collectors.toList()).get(0);

            List<TextNode> listingDateTextNodes = propertyInfoPage.select(".dp-price-history__item .dp-price-history__item-date").textNodes();

            return new PropertyInfo(
                    location,
                    propertyInfoPage.select("article.dp-sidebar-wrapper__summary .ui-pricing__main-price.ui-text-t4").textNodes().get(0).toString().trim(),
                    propertyInfoPage.select("article.dp-sidebar-wrapper__summary .ui-property-summary__address").textNodes().get(0).toString().trim().replaceAll("&amp;", "&"),
                    propertyInfoPage.select("article.dp-sidebar-wrapper__summary .ui-property-summary__title.ui-title-subgroup").textNodes().get(0).toString().trim(),
                    propertyPageUri.toString(),
                    distanceBetween(
                            SCHOOLS.get(searchParameters.getSchoolId()).getLatitude(),
                            SCHOOLS.get(searchParameters.getSchoolId()).getLongitude(),
                            location.getLatitude(),
                            location.getLongitude()
                    ),
                    distanceBetween(
                            TOOTING_COMMON_LATITUDE,
                            TOOTING_COMMON_LONGITUDE,
                            location.getLatitude(),
                            location.getLongitude()
                    ),
                    listingDateTextNodes.isEmpty() ? null : LocalDate.parse(listingDateTextNodes.get(0).toString().trim().replaceAll("(?<=\\d)(st|nd|rd|th)", ""), ZOOPLA_DATE_FORMAT)

            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
