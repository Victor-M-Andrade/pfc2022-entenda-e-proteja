package br.fai.ep.epWeb.controller;

import br.fai.ep.epEntities.Noticia;
import br.fai.ep.epEntities.Parceiro;
import br.fai.ep.epEntities.Usuario;
import br.fai.ep.epWeb.helper.AnonymizeData;
import br.fai.ep.epWeb.helper.FoldersName;
import br.fai.ep.epWeb.security.provider.EpAuthenticationProvider;
import br.fai.ep.epWeb.service.impl.NewsWebServiceImpl;
import br.fai.ep.epWeb.service.impl.PartnerWebServiceImpl;
import br.fai.ep.epWeb.service.impl.UserWebServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AccountController {
    private final UserWebServiceImpl service = new UserWebServiceImpl();
    private final PartnerWebServiceImpl partnerWebService = new PartnerWebServiceImpl();
    private final NewsWebServiceImpl newsWebService = new NewsWebServiceImpl();
    private final EpAuthenticationProvider epAuthenticationProvider = new EpAuthenticationProvider();

    private final String USER_ID = "userId";
    private final String SENDED_EMAIL = "sendedEmail";
    private final String MY_USER_OBJECT = "myUser";
    private final String EMAIL_NOT_FOUND = "emailNotFound";
    private final String DELETE_USER_ERROR = "deleteUserError";
    private final String OLD_USER_PASSWORD = "oldUserPassword";
    private final String ANONYMIZE_USER_ERROR = "anonymizeUserError";
    private final String AUTHENTICATION_ERROR = "authenticationError";
    private final String CHANGE_PASSWORD_ERROR = "changePasswordError";
    private final String ALREADY_REGISTERED_EMAIL = "alreadyRegisteredEmail";
    private final String ACCOUNT_CREATION_PROBLEMS = "accountCreationProblems";

    private boolean sendedEmail = false;
    private boolean emailNotFound = false;
    private boolean deleteUserError = false;
    private boolean anonymizeUserError = false;
    private boolean changePasswordError = false;
    private boolean authenticationError = false;
    private boolean triedPasswordChange = false;
    private boolean alreadyregisteredEmail = false;
    private boolean accountCreationProblems = false;

    private Usuario temporaryUser = null;

    @GetMapping("/account/login")
    public String getLoginPage(final Model model, final Usuario user) {
        model.addAttribute(AUTHENTICATION_ERROR, authenticationError);
        authenticationError = false;
        return FoldersName.ACCOUNT_FOLDER + "/login";
    }

    @GetMapping("/account/login-error")
    public String getLoginPageWithMessageError() {
        authenticationError = true;
        return "redirect:/account/login";
    }

    @GetMapping("/account/register")
    public String getRegisterPage(final Model model, Usuario user) {
        model.addAttribute(ALREADY_REGISTERED_EMAIL, alreadyregisteredEmail);
        if (alreadyregisteredEmail) {
            alreadyregisteredEmail = false;
        }

        model.addAttribute(ACCOUNT_CREATION_PROBLEMS, accountCreationProblems);
        if (accountCreationProblems) {
            accountCreationProblems = false;
        }

        if (temporaryUser != null) {
            user = temporaryUser;
            temporaryUser = null;
        }
        model.addAttribute(MY_USER_OBJECT, user);

        return FoldersName.ACCOUNT_FOLDER + "/register";
    }

    @PostMapping("/account/create")
    private String createUser(final Usuario user) {
        final long newIdUser = service.create(user);
        if (newIdUser == -1) {
            accountCreationProblems = true;
            temporaryUser = user;
            return "redirect:/account/register";
        } else if (newIdUser == -2) {
            alreadyregisteredEmail = true;
            temporaryUser = user;
            return "redirect:/account/register";
        }

        return "redirect:/user/profile";
    }

    @GetMapping("/account/forgot-my-password")
    public String getForgotMyPassowordPage(final Model model, Usuario user) {
        model.addAttribute(SENDED_EMAIL, sendedEmail);
        if (sendedEmail) {
            sendedEmail = false;
        }

        model.addAttribute(EMAIL_NOT_FOUND, emailNotFound);
        if (emailNotFound) {
            emailNotFound = false;
        }

        if (temporaryUser != null) {
            user = temporaryUser;
            temporaryUser = null;
        }
        model.addAttribute(MY_USER_OBJECT, user);

        return FoldersName.ACCOUNT_FOLDER + "/password";
    }

    @PostMapping("/account/request-password-change")
    public String requestPasswordChange(final Model model, final Usuario user) {
        sendedEmail = new UserWebServiceImpl().forgotPassword(user.getEmail());
        if (!sendedEmail) {
            emailNotFound = true;
            temporaryUser = user;
        }
        return "redirect:/account/forgot-my-password";
    }

    @GetMapping("/account/change-my-password")
    public String getChangeMyPasswordPage(final Model model) {
        final Usuario authenticatedUser = epAuthenticationProvider.getAuthenticatedUser();
        if (authenticatedUser == null) {
            return "redirect:/not-found";
        }

        final Usuario user = (Usuario) service.readById(authenticatedUser.getId());
        if (user == null && !triedPasswordChange) {
            changePasswordError = false;
            triedPasswordChange = false;
            return "redirect:/not-found";
        }
        triedPasswordChange = false;

        model.addAttribute(USER_ID, user.getId());
        model.addAttribute(MY_USER_OBJECT, user);
        model.addAttribute(OLD_USER_PASSWORD, user.getSenha());
        model.addAttribute(CHANGE_PASSWORD_ERROR, changePasswordError);
        if (changePasswordError) {
            changePasswordError = false;
        }

        return FoldersName.ACCOUNT_FOLDER + "/reset-password";
    }

    @PostMapping("/account/update-password")
    public String updatePassword(final Model model, final Usuario user) {
        triedPasswordChange = true;
        final Usuario myUser = (Usuario) service.readById(user.getId());
        if (myUser == null) {
            changePasswordError = true;
            return "redirect:/account/change-my-password";
        }

        myUser.setSenha(user.getSenha());
        changePasswordError = !service.update(myUser);
        if (changePasswordError) {
            return "redirect:/account/change-my-password";
        }
        triedPasswordChange = false;
        return "redirect:/user/profile";
    }

    @GetMapping("/account/check-exists-user/{id}")
    public String checkExistsUser(@PathVariable final long id) {
        if (service.readByIdForUpdatePassword(id) != null) {
            System.out.println("Lemos um usuário: ");
            return "redirect:/account/change-user-password/" + id;
        }
        return "redirect:/not-found";
    }

    @GetMapping("/account/change-user-password/{id}")
    public String getChangeUserPasswordPage(@PathVariable final long id, final Model model, final Usuario user) {
        model.addAttribute(USER_ID, id);
        model.addAttribute(CHANGE_PASSWORD_ERROR, changePasswordError);
        if (changePasswordError) {
            changePasswordError = false;
        }

        return FoldersName.ACCOUNT_FOLDER + "/change-password";
    }

    @PostMapping("/account/update-user-password")
    public String updateUserPassword(final Model model, final Usuario user) {
        final Usuario myUser = service.readByIdForUpdatePassword(user.getId());
        if (myUser == null) {
            changePasswordError = true;
            return "redirect:/account/change-user-password/" + user.getId();
        }

        myUser.setSenha(user.getSenha());
        changePasswordError = !service.updateForgottenUserPassword(myUser);
        if (changePasswordError) {
            return "redirect:/account/change-user-password/" + user.getId();
        }

        return "redirect:/account/login";
    }

    @GetMapping("/account/confirm-delete-account")
    public String confirmDeleteAccount() {
        final Usuario authenticatedUser = epAuthenticationProvider.getAuthenticatedUser();
        if (authenticatedUser == null) {
            deleteUserError = false;
            return "redirect:/user/profile";
        }

        final Usuario user = (Usuario) service.readById(authenticatedUser.getId());
        if (user.isAdministrador() || user.isAutor() || user.isParceiro()) {
            return "redirect:/account/request-use-data";
        }

        deleteUserError = !service.delete(authenticatedUser.getId());
        if (deleteUserError) {
            return "redirect:/user/profile";
        }
        return "redirect:/log-out";
    }

    @GetMapping("/account/request-use-data")
    public String getRequestUseDataPage(final Model model) {
        final Usuario authenticatedUser = epAuthenticationProvider.getAuthenticatedUser();
        if (authenticatedUser == null) {
            return "redirect:/not-found";
        }

        final Usuario usuario = (Usuario) service.readById(authenticatedUser.getId());
        final String originalName = usuario.getNome();
        final String anonymousName = AnonymizeData.anonymizeData(usuario.getNome());

        model.addAttribute(USER_ID, authenticatedUser.getId());
        model.addAttribute("anonymous", String.format("Exemplo: nome cadastrado %s | Próximas consultas em que for mensionado: %s",
                originalName, anonymousName));

        boolean userIsPartner = false;
        if (usuario.isParceiro()) {
            final Map<String, Long> criteria = new HashMap<>();
            criteria.put(Parceiro.PARTNER_TABLE.ID_USER_COLUMN, usuario.getId());
            final List<Parceiro> partnerList = (List<Parceiro>) partnerWebService.readByCriteria(criteria);
            if (partnerList != null && !partnerList.isEmpty() && partnerList.size() == 1) {
                model.addAttribute("dataPartner", partnerList.get(0));
                userIsPartner = true;
            }
        }
        model.addAttribute("userIsParner", userIsPartner);

        boolean userIsAuthor = false;
        if (usuario.isAutor()) {
            final Map<String, Long> criteria = new HashMap<>();
            criteria.put(Noticia.NEWS_TABLE.ID_AUTHOR_COLUMN, usuario.getId());
            final List<Noticia> newsList = (List<Noticia>) newsWebService.readByCriteria(criteria);
            if (newsList != null && !newsList.isEmpty()) {
                model.addAttribute("newsList", newsList);
                userIsAuthor = true;
            }
        }
        model.addAttribute("userIsAuthor", userIsAuthor);

        model.addAttribute(DELETE_USER_ERROR, deleteUserError);
        deleteUserError = false;

        model.addAttribute(ANONYMIZE_USER_ERROR, anonymizeUserError);
        anonymizeUserError = false;

        return FoldersName.ACCOUNT_FOLDER + "/confirmar-exclusao";
    }

    @GetMapping("/account/confirm-delete-my-account")
    public String confirmeDeleteMyAccount() {
        final Usuario authenticatedUser = epAuthenticationProvider.getAuthenticatedUser();
        if (authenticatedUser == null) {
            return "redirect:/not-found";
        }

        deleteUserError = !service.delete(authenticatedUser.getId());
        if (deleteUserError) {
            return "redirect:/account/request-use-data";
        }
        return "redirect:/log-out";
    }

    @GetMapping("/account/anonymize-my-account")
    public String confirmeAnonymizeMyAccount() {
        final Usuario authenticatedUser = epAuthenticationProvider.getAuthenticatedUser();
        if (authenticatedUser == null) {
            return "redirect:/not-found";
        }

        anonymizeUserError = !service.anonymizeUser(authenticatedUser.getId());
        if (anonymizeUserError) {
            return "redirect:/account/request-use-data";
        }
        return "redirect:/log-out";
    }

}