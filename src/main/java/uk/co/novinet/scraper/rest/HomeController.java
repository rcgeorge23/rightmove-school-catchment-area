package uk.co.novinet.scraper.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import uk.co.novinet.scraper.RightMoveSearchService;
import uk.co.novinet.scraper.ZooplaSearchService;
import uk.co.novinet.scraper.dto.PropertyInfo;
import uk.co.novinet.scraper.dto.School;
import uk.co.novinet.scraper.dto.SearchParameters;

import java.io.IOException;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class HomeController {

    public static final Map<Long, School> SCHOOLS = Map.of(
            1L, new School(1, "Graveney Secondary School", 51.4233841f, -0.1520402f)
    );

    @Autowired
    private RightMoveSearchService rightMoveSearchService;

    @Autowired
    private ZooplaSearchService zooplaMoveSearchService;

    @GetMapping("/")
    public ModelAndView get() {
        ModelAndView modelAndView = new ModelAndView("home");
        modelAndView.addObject("schools", SCHOOLS.values());
        return modelAndView;
    }

    @PostMapping("/search")
    public ModelAndView search(SearchParameters searchParameters) throws IOException {
        ModelAndView modelAndView = new ModelAndView("searchResults");
        List<PropertyInfo> propertyInfos = rightMoveSearchService.search(searchParameters);
        propertyInfos.addAll(zooplaMoveSearchService.search(searchParameters));
        propertyInfos.sort(Comparator.comparing(PropertyInfo::getDateAdded).reversed());
        modelAndView.addObject("propertyInfoList", propertyInfos);
        modelAndView.addObject("school", SCHOOLS.get(searchParameters.getSchoolId()));
        return modelAndView;
    }

}