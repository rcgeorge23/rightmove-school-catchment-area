package uk.co.novinet.scraper.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import uk.co.novinet.scraper.RightMoveSearchService;
import uk.co.novinet.scraper.dto.SearchParameters;

import java.io.IOException;

@Controller
@Slf4j
public class HomeController {

    @Autowired
    private RightMoveSearchService rightMoveSearchService;

    @GetMapping("/")
    public String get() {
        return "home";
    }

    @PostMapping("/search")
    public ModelAndView search(SearchParameters searchParameters) throws IOException {
        ModelAndView modelAndView = new ModelAndView("searchResults");
        modelAndView.addObject("propertyInfoList", rightMoveSearchService.search(searchParameters));
        modelAndView.addObject("test", "testytest");
        return modelAndView;
    }

}