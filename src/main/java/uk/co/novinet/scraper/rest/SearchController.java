package uk.co.novinet.scraper.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.co.novinet.scraper.dto.PropertyInfo;
import uk.co.novinet.scraper.RightMoveSearchService;
import uk.co.novinet.scraper.dto.DataContainer;
import uk.co.novinet.scraper.dto.SearchParameters;

import java.io.IOException;
import java.util.List;

@RestController
@Slf4j
public class SearchController {

    @Autowired
    private RightMoveSearchService rightMoveSearchService;

    @PostMapping("/search")
    public DataContainer search(SearchParameters searchParameters) throws IOException {
        List<PropertyInfo> propertyInfoList = rightMoveSearchService.search(searchParameters);
        return new DataContainer(1, 100, propertyInfoList.size(), 1, propertyInfoList);
    }

}