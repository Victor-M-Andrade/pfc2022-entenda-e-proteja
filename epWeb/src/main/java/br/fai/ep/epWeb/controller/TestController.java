package br.fai.ep.epWeb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {

    @GetMapping("/knowledge-test/questionnaire")
    public String getQuestionnairePage() {
        return "quiz/teste";
    }
}