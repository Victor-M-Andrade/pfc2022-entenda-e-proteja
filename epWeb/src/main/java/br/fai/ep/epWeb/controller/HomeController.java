package br.fai.ep.epWeb.controller;

import br.fai.ep.epEntities.DTO.MailDto;
import br.fai.ep.epEntities.DTO.NewsDto;
import br.fai.ep.epWeb.helper.FoldersName;
import br.fai.ep.epWeb.service.impl.NewsWebServiceImpl;
import br.fai.ep.epWeb.service.impl.UserWebServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class HomeController {

    private final UserWebServiceImpl userWebService = new UserWebServiceImpl();
    private final NewsWebServiceImpl newsWebService = new NewsWebServiceImpl();

    private final String EXISTS_NEWS = "existsNews";
    private final String SENDED_EMAIL = "sendedEmail";
    private final String SENDED_EMAIL_ERROR = "sendedEmailError";

    private boolean sendedEmail = false;
    private boolean sendEmailError = false;

    private MailDto temporaryMailDto = null;

    @GetMapping("/")
    public String getHomePage(final Model model) {
        final List<NewsDto> newsDtoList = newsWebService.readLastNewsDtoWithLimit(5);

        boolean existNews = true;
        if (newsDtoList == null || newsDtoList.isEmpty()) {
            existNews = false;
        }

        model.addAttribute(EXISTS_NEWS, existNews);
        model.addAttribute("newsList", newsDtoList);
        return "home";
    }

    @GetMapping("/about-project")
    public String getAboutProjectPage() {
        return FoldersName.CONTACT_FOLDER + "/sobre";
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

        return FoldersName.CONTACT_FOLDER + "/fale_conosco";
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
        return FoldersName.CONTRACTS_FOLDER + "/politica_privacidade";
    }

    @GetMapping("/terms-conditions")
    public String getTermsConditionsPage() {
        return FoldersName.CONTRACTS_FOLDER + "/termos_uso";
    }
}