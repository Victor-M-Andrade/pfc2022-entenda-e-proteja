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

    @GetMapping("/account/login")
    public String getLoginPage(final Model model, final Usuario usuario) {
        model.addAttribute(AUTHENTICATION_ERROR, false);
        return "conta/login";
    }

    @PostMapping("/account/authenticate")
    public String getAuthenticatePage(final Model model, final Usuario user) {
        final Usuario myUser = new UsuarioServiceImpl().authentication(user.getEmail(), user.getSenha());
        if (myUser == null) {
            model.addAttribute(AUTHENTICATION_ERROR, true);
            return "conta/login";
        }

        return "redirect:/user/profile/" + myUser.getId();
    }

    @GetMapping("/account/register")
    public String getRegisterPage(final Model model, final Usuario user) {
        return "conta/register";
    }

    @PostMapping("/account/create")
    private String createUser(final Usuario user, final Model model) {
        final long newIdUser = service.create(user);
        if (newIdUser == -1) {
            return "redirect:/account/register";
        }

        return "redirect:/user/profile/" + newIdUser;
    }

    @GetMapping("/account/forgot-my-password")
    public String getForgotMyPassowordPage(final Model model, final Usuario usuario) {
        model.addAttribute(SENDED_EMAIL, false);
        model.addAttribute(EMAIL_NOT_FOUND, false);
        return "conta/password";
    }

    @PostMapping("/account/request-password-change")
    public String requestPasswordChange(final Model model, final Usuario user) {
        final boolean sendedEmail = new UsuarioServiceImpl().forgotPassword(user.getEmail());
        if (sendedEmail) {
            model.addAttribute(SENDED_EMAIL, true);
            model.addAttribute(EMAIL_NOT_FOUND, false);
            return "conta/password";
        }
        model.addAttribute(SENDED_EMAIL, false);
        model.addAttribute(EMAIL_NOT_FOUND, true);
        return "conta/password";
    }

    @GetMapping("/account/log-out")
    public String singOut() {
        return "redirect:/";
    }

    @GetMapping("/user/profile/{id}")
    public String getUserProfilePage(@PathVariable final long id, final Model model) {
        final Usuario user = (Usuario) service.readById(id);
        model.addAttribute("myUser", user);
        model.addAttribute("dateCreate", service.getCreationDateAndTime(user.getDataHora()));
        return "/usuario/perfil";
    }

    @GetMapping("/user/edit")
    public String getUserEditPage() {
        return "/usuario/editar_perfil";
    }
}