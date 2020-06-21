package uk.co.novinet.scraper.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataContainer {
    long page; // the 1-based number of the current page
    long rowNum; // the page size - the maximal number of records in a page (the last page can contains less records)
    long records; // total records (on all pages) in the grid
    long total; // total number of pages for the grid
    List<? extends Object> rows;
}
