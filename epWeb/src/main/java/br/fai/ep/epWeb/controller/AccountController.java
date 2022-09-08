package br.fai.ep.epWeb.controller;

import br.fai.ep.epEntities.Usuario;
import br.fai.ep.epWeb.service.ServiceInterface;
import br.fai.ep.epWeb.service.impl.UsuarioServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AccountController {
    private final ServiceInterface service = new UsuarioServiceImpl();
    private final String AUTHENTICATION_ERROR = "authenticationError";
    private final String EMAIL_NOT_FOUND = "emailNotFound";
    private final String SENDED_EMAIL = "sendedEmail";
    public final String MY_USER_REFERENCE = "myUser";
    public final String USER_CREATION_DATE = "dateCreate";
    public final String ALREADY_REGISTERED_EMAIL = "alreadyRegisteredEmail";
    public final String ACCOUNT_CREATION_PROBLEMS = "accountCreationProblems";

    public boolean sendedEmail = false;
    public boolean emailNotFound = false;
    public boolean authenticationError = false;
    public boolean alreadyregisteredEmail = false;
    public boolean accountCreationProblems = false;

    @GetMapping("/account/login")
    public String getLoginPage(final Model model, final Usuario usuario) {
        model.addAttribute(AUTHENTICATION_ERROR, authenticationError);
        if (authenticationError) {
            authenticationError = false;
        }

        return "conta/login";
    }

    @PostMapping("/account/authenticate")
    public String getAuthenticatePage(final Model model, final Usuario user) {
        final Usuario myUser = new UsuarioServiceImpl().authentication(user.getEmail(), user.getSenha());
        if (myUser == null) {
            authenticationError = true;
            return "redirect:/account/login";
        }

        return "redirect:/user/profile/" + myUser.getId();
    }

    @GetMapping("/account/register")
    public String getRegisterPage(final Model model, final Usuario user) {
        model.addAttribute(ALREADY_REGISTERED_EMAIL, alreadyregisteredEmail);
        if (alreadyregisteredEmail) {
            alreadyregisteredEmail = false;
        }

        model.addAttribute(ACCOUNT_CREATION_PROBLEMS, accountCreationProblems);
        if (accountCreationProblems) {
            accountCreationProblems = false;
        }
        return "conta/register";
    }

    @PostMapping("/account/create")
    private String createUser(final Usuario user, final Model model) {
        final long newIdUser = service.create(user);
        if (newIdUser == -1) {
            accountCreationProblems = true;
            return "redirect:/account/register";
        } else if (newIdUser == -2) {
            alreadyregisteredEmail = true;
            return "redirect:/account/register";
        }

        return "redirect:/user/profile/" + newIdUser;
    }

    @GetMapping("/account/forgot-my-password")
    public String getForgotMyPassowordPage(final Model model, final Usuario usuario) {
        model.addAttribute(SENDED_EMAIL, sendedEmail);
        if (sendedEmail) {
            sendedEmail = false;
        }

        model.addAttribute(EMAIL_NOT_FOUND, emailNotFound);
        if (emailNotFound) {
            emailNotFound = false;
        }

        return "conta/password";
    }

    @PostMapping("/account/request-password-change")
    public String requestPasswordChange(final Model model, final Usuario user) {
        sendedEmail = new UsuarioServiceImpl().forgotPassword(user.getEmail());
        if (sendedEmail) {
            emailNotFound = false;
            return "redirect:/account/forgot-my-password";
        }
        emailNotFound = true;
        return "redirect:/account/forgot-my-password";
    }

    @GetMapping("/account/log-out")
    public String singOut() {
        return "redirect:/";
    }

    @GetMapping("/user/profile/{id}")
    public String getUserProfilePage(@PathVariable final long id, final Model model) {
        final Usuario user = (Usuario) service.readById(id);
        model.addAttribute(MY_USER_REFERENCE, user);
        model.addAttribute(USER_CREATION_DATE, service.getCreationDateAndTime(user.getDataHora()));
        return "/usuario/perfil";
    }

    @GetMapping("/user/edit")
    public String getUserEditPage() {
        return "/usuario/editar_perfil";
    }
}