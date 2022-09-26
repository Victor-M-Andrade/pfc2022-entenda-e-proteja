package br.fai.ep.epWeb.controller;

import br.fai.ep.epEntities.DTO.MailDto;
import br.fai.ep.epWeb.service.impl.UserWebServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomeController {

    @Autowired
    private UserWebServiceImpl userWebService;

    private final String SENDED_EMAIL = "sendedEmail";
    private final String SENDED_EMAIL_ERROR = "sendedEmailError";

    private boolean sendedEmail = false;
    private boolean sendEmailError = false;

    private MailDto temporaryMailDto = null;

    @GetMapping("/")
    public String getHomePage() {
        return "home";
    }

    @GetMapping("/about-project")
    public String getAboutProjectPage() {
        return "contatos/sobre";
    }

    @GetMapping("/contact-us")
    public String getContactUsPage(final Model model, MailDto mail) {
        model.addAttribute(SENDED_EMAIL, sendedEmail);
        if (sendedEmail) {
            sendedEmail = false;
        }

        model.addAttribute(SENDED_EMAIL_ERROR, sendEmailError);
        if (sendEmailError) {
            sendEmailError = false;
        }

        if (temporaryMailDto != null) {
            mail = temporaryMailDto;
            temporaryMailDto = null;
        }
        model.addAttribute("mailDto", mail);

        return "contatos/fale_conosco";
    }

    @PostMapping("/send-email")
    public String sendEmail(final MailDto mail) {
        sendedEmail = userWebService.sendEmail(mail);
        if (!sendedEmail) {
            sendEmailError = true;
            temporaryMailDto = mail;
        }

        return "redirect:/contact-us";
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