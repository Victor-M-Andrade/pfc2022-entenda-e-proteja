package br.fai.ep.epWeb.controller;

import br.fai.ep.epEntities.Usuario;
import br.fai.ep.epWeb.service.BaseWebService;
import br.fai.ep.epWeb.service.WebServiceInterface;
import br.fai.ep.epWeb.service.impl.UserWebServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class UserController {
    public static final String DELETE_USER_ERROR = "deleteUserError";
    public static final String ANONYMIZE_USER_ERROR = "anonymizeUserError";

    public static boolean deleteUserError = false;
    public static boolean anonymizeUserError = false;

    private final WebServiceInterface service = new UserWebServiceImpl();

    private final String USER_ID = "userId";
    private final String EXISTS_USERS = "existsUsers";
    private final String REGISTERED_USERS = "registeredUsers";
    private final String MY_USER_REFERENCE = "myUser";
    private final String DATA_UPDATE_ERROR = "updateDataError";
    private final String USER_CREATION_DATE = "dateCreate";
    private final String IS_ADMINISTRATOR_USER = "isAdministrator";

    private boolean triedPasswordChange = false;
    private boolean updateUserDataError = false;

    private Usuario temporaryUser = null;

    @GetMapping("/user/profile/{id}")
    public String getMyUserProfilePage(@PathVariable final long id, final Model model) {
        final Usuario user = (Usuario) service.readById(id);
        model.addAttribute(MY_USER_REFERENCE, user);
        model.addAttribute(USER_ID, user.getId());
        model.addAttribute(USER_CREATION_DATE, service.getCreationDateAndTime(user.getDataHora()));
        model.addAttribute(IS_ADMINISTRATOR_USER, user.isAdministrador());

        model.addAttribute(DELETE_USER_ERROR, deleteUserError);
        if (deleteUserError) {
            deleteUserError = false;
        }
        return "/usuario/perfil";
    }

    @GetMapping("/user/edit/{id}")
    public String getMyUserEditPage(@PathVariable final int id, final Model model) {
        try {
            final Usuario user = (Usuario) service.readById(id);
            if (user != null) {
                triedPasswordChange = false;
                temporaryUser = null;
                model.addAttribute(user);
                model.addAttribute(USER_ID, user.getId());
                model.addAttribute(DATA_UPDATE_ERROR, updateUserDataError);
                updateUserDataError = false;
                return "/usuario/editar_perfil";
            }

            if (triedPasswordChange && temporaryUser != null) {
                triedPasswordChange = false;
                model.addAttribute(temporaryUser);
                model.addAttribute(USER_ID, temporaryUser.getId());
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
    public String updateMyUserData(@RequestParam("fromMyFile") final MultipartFile file, final Usuario user, final Model model) {
        triedPasswordChange = true;
        temporaryUser = user;

        if (file.isEmpty()) {
            updateUserDataError = !service.update(user);
            if (updateUserDataError) {
                return "redirect:/user/edit/" + user.getId();
            }
            triedPasswordChange = false;
            return "redirect:/user/profile/" + user.getId();
        }

        final String newNameFile = service.buildNameNewFile(user);
        final String nameFileWithExtension = service.prepareNameWithExtension(file.getOriginalFilename(), newNameFile);
        if (nameFileWithExtension == null) {
            updateUserDataError = !service.update(user);
            if (updateUserDataError) {
                return "redirect:/user/edit/" + user.getId();
            }
            triedPasswordChange = false;
            return "redirect:/user/profile/" + user.getId();
        }

        final String pathImage = service.saveFileInProfile(file, BaseWebService.PATH_IMAGENS_USERS, nameFileWithExtension, newNameFile);
        if (pathImage != null) {
            user.setPathImageProfile(pathImage);
        }

        updateUserDataError = !service.update(user);
        if (updateUserDataError) {
            return "redirect:/user/edit/" + user.getId();
        }
        triedPasswordChange = false;
        return "redirect:/user/profile/" + user.getId();
    }
}