package uk.co.novinet.scraper.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class School {
    long id;
    String name;
    float latitude;
    float longitude;
}
