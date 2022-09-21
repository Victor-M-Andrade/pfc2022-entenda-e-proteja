package br.fai.ep.epWeb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PartnerController {

    @GetMapping("/partner/list")
    public String getPartnerListPage() {
        return "parceiro/consultoria_list";
    }

    @GetMapping("/partner/detail")
    public String getPartnerDetailPage() {
        return "parceiro/consultoria_info";
    }
}