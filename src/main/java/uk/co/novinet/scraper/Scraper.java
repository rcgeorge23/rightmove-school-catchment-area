package uk.co.novinet.scraper;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Scraper {

    private static final float GRAVENEY_LATITUDE = 51.4234f;
    private static final float GRAVENEY_LONGITUDE = 0.1520f;

    private static final float TOOTING_COMMON_LATITUDE = 51.427999F;
    private static final float TOOTING_COMMON_LONGITUDE = 0.148449f;

    private static final int MIN_PRICE = 650000;
    private static final int MAX_PRICE = 1250000;
    private static final int MIN_BEDROOMS = 3;

    private static final String BASE_URL = "https://www.rightmove.co.uk";
    private static final DateTimeFormatter RIGHTMOVE_DATE_FORMAT = DateTimeFormatter.ofPattern("dd MMMM yyyy");

    private static final Pattern PATTERN_LOCATION = Pattern.compile("\\/\\/media.rightmove.co.uk\\/map\\/_generate\\?latitude=(?<latitude>[-+]?[0-9]*\\.?[0-9]+)\\&longitude=(?<longitude>[-+]?[0-9]*\\.?[0-9]+)");

    public static void main(String[] args) throws IOException {
        List<PropertyInfo> propertyInfos = findPropertyPageLinks().stream().map(Scraper::findPropertyInfo).filter(propertyInfo ->
                propertyInfo.getDistanceToGraveneySchoolMeters() < 500).sorted((propertyInfo1, propertyInfo2) -> propertyInfo2.getDateAdded().compareTo(propertyInfo1.getDateAdded())).collect(Collectors.toList());

        try (CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(System.out), CSVFormat.DEFAULT
                .withHeader(
                        "Address",
                        "Description",
                        "Price",
                        "Distance to Graveney School (m)",
                        "Distance to Tooting Common (m)",
                        "Date added to rightmove",
                        "Link"))) {
            propertyInfos.forEach(propertyInfo -> {
                try {
                    printer.printRecord(
                            propertyInfo.getAddress(),
                            propertyInfo.getDescription(),
                            propertyInfo.getPrice(),
                            propertyInfo.getDistanceToGraveneySchoolMeters(),
                            propertyInfo.getDistanceToTootingCommonMeters(),
                            propertyInfo.getDateAdded(),
                            propertyInfo.getUri()
                    );
                } catch (IOException e) {
                    throw new RuntimeException(e);
                };
            });
        }
    }

    private static List<URI> findPropertyPageLinks() throws IOException {
        Document doc = Jsoup.connect(BASE_URL + "/property-for-sale/find.html?searchType=SALE&locationIdentifier=REGION%5E70343&insId=1&radius=0.0&minPrice=" + MIN_PRICE + "&maxPrice=" + MAX_PRICE + "&minBedrooms=" + MIN_BEDROOMS + "&maxBedrooms=&displayPropertyType=houses&maxDaysSinceAdded=&_includeSSTC=on&sortByPriceDescending=&primaryDisplayPropertyType=&secondaryDisplayPropertyType=&oldDisplayPropertyType=&oldPrimaryDisplayPropertyType=&newHome=&auction=false").get();
        return doc.select(".propertyCard-link").stream().filter(element -> !"".equals(element.attr("href").trim())).map(element -> URI.create(BASE_URL + element.attr("href"))).distinct().collect(Collectors.toList());
    }

    private static PropertyInfo findPropertyInfo(URI propertyPageUri) {
        try {
            Document propertyInfoPage = Jsoup.connect(propertyPageUri.toString()).get();

            Location location = propertyInfoPage.select("img[alt=\"Get map and local information\"]").stream().map(element -> {
                Matcher matcher = PATTERN_LOCATION.matcher(element.attr("src"));
                if (matcher.find()) {
                    return new Location(
                            Float.parseFloat(matcher.group("latitude")),
                            Math.abs(Float.parseFloat(matcher.group("longitude")))
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
        return (float) (earthRadius * c);
    }
}
