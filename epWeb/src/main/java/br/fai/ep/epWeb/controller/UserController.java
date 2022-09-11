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
public class UserController {
    private final ServiceInterface service = new UsuarioServiceImpl();

    private final String MY_USER_REFERENCE = "myUser";
    private final String USER_CREATION_DATE = "dateCreate";
    private final String USER_ID = "userId";
    private final String DATA_UPDATE_ERROR = "updateDataError";

    private boolean triedPasswordChange = false;
    private boolean updateUserDataError = false;

    private Usuario temporaryUser = null;

    @GetMapping("/user/profile/{id}")

    public String getUserProfilePage(@PathVariable final long id, final Model model) {
        final Usuario user = (Usuario) service.readById(id);
        model.addAttribute(MY_USER_REFERENCE, user);
        model.addAttribute(USER_ID, user.getId());
        model.addAttribute(USER_CREATION_DATE, service.getCreationDateAndTime(user.getDataHora()));
        return "/usuario/perfil";
    }

    @GetMapping("/user/edit/{id}")
    public String getUserEditPage(@PathVariable final int id, final Model model) {
        try {
            final Usuario user = (Usuario) service.readById(id);
            if (user != null) {
                triedPasswordChange = false;
                temporaryUser = null;
                model.addAttribute(user);
                model.addAttribute(DATA_UPDATE_ERROR, updateUserDataError);
                updateUserDataError = false;
                return "/usuario/editar_perfil";
            }

            if (triedPasswordChange && temporaryUser != null) {
                triedPasswordChange = false;
                model.addAttribute(temporaryUser);
                temporaryUser = null;

                model.addAttribute(DATA_UPDATE_ERROR, updateUserDataError);
                updateUserDataError = false;
                return "/usuario/editar_perfil";
            }

            triedPasswordChange = false;
            updateUserDataError = false;
            return "redirect:/not-found";

        } catch (final Exception ex) {
            triedPasswordChange = false;
            updateUserDataError = false;
            return "redirect:/not-found";
        }
    }

    @PostMapping("/user/update-user-data")
    public String updateUserData(final Usuario user, final Model model) {
        triedPasswordChange = true;
        temporaryUser = user;

        updateUserDataError = !service.update(user);
        if (updateUserDataError) {
            return "redirect:/user/edit/" + user.getId();
        }
        triedPasswordChange = false;
        temporaryUser = null;
        return "redirect:/user/profile/" + user.getId();
    }
}