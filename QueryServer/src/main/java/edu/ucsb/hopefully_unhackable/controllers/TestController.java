package edu.ucsb.hopefully_unhackable.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
//@RestController // Equivalent to @Controller, @ReponseBody returns data rather than a view
public class TestController {
    @RequestMapping("/")
    public String index() {
        return "index";
    }
}
