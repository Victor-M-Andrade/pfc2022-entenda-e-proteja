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

    @GetMapping("/contact-us")
    public String getContactUsPage() {
        return "contatos/fale_conosco";
    }

    @GetMapping("/not-found")
    public String getNotFoundPage() {
        return "exception/not-found";
    }

    @GetMapping("/privacy-policy")
    public String getPrivacyPolicyPage() {
        return "contratos/politica_privacidade";
    }

    @GetMapping("/terms-conditions")
    public String getTermsConditionsPage() {
        return "contratos/termos_uso";
    }
}