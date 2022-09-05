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

    @GetMapping("/account/authenticate")
    public String getAuthenticatePage() {
        return "conta/login";
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
    public String getForgotMyPassowordPage() {
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