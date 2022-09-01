package br.fai.ep.epWeb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String getHomePage() {
        return "home";
    }

    @GetMapping("/about-project")
    public String getAboutProjectPage() {
        return "contatos/sobre";
    }

    @GetMapping("/not-found")
    public String getNotFoundPage() {
        return "/exception/not-found";
    }
}