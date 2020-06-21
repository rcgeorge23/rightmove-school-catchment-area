package uk.co.novinet.scraper.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import uk.co.novinet.scraper.RightMoveSearchService;

@Controller
@Slf4j
public class HomeController {

    @Autowired
    private RightMoveSearchService rightMoveSearchService;

    @GetMapping("/")
    public String get() {
        return "home";
    }

}