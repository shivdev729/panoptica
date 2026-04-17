package com.artifactexplorer;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

@Controller
public class MainController {
    @RequestMapping("/")
    public String home(Model model) {
        return "museum";
            //    "BharatMusuemUI"
    }
}
