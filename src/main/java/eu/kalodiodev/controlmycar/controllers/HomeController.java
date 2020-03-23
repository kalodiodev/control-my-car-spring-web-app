package eu.kalodiodev.controlmycar.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping({"", "/", "index.html"})
    public String home() {
        return "index";
    }
}
