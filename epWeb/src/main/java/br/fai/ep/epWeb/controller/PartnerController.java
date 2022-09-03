package br.fai.ep.epWeb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PartnerController {

    @GetMapping("/partners/list")
    public String getPartnerIdPage() {
        return "contatos/consultoria";
    }
}