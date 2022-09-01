package br.fai.ep.epWeb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccountController {

    @GetMapping("/account/authenticate")
    public String getAuthenticatePage() {
        return "conta/login";
    }

    @GetMapping("/account/register")
    public String getRegisterPage() {
        return "conta/register";
    }

    @GetMapping("/account/forgot-my-password")
    public String getForgotMyPassowordPage() {
        return "conta/password";
    }

    @GetMapping("/account/log-out")
    public String singOut() {
        return "redirect:/";
    }

    @GetMapping("/user/profile")
    public String getUserProfilePage() {
        return "/usuario/perfil";
    }

    @GetMapping("/user/edit")
    public String getUserEditPage() {
        return "/usuario/editar_perfil";
    }

}