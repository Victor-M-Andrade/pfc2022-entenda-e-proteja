package br.fai.ep.epWeb.controller;

import br.fai.ep.epEntities.Parceiro;
import br.fai.ep.epEntities.Usuario;
import br.fai.ep.epWeb.helper.FoldersName;
import br.fai.ep.epWeb.service.impl.PartnerWebServiceImpl;
import br.fai.ep.epWeb.service.impl.UserWebServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AdministratorController {

    private final UserWebServiceImpl userWebService = new UserWebServiceImpl();
    private final PartnerWebServiceImpl partnerWebService = new PartnerWebServiceImpl();

    private final String USER_ID = "userId";
    private final String EXISTS_USERS = "existsUsers";
    private final String REGISTERED_USERS = "registeredUsers";
    private final String DATA_UPDATE_ERROR = "updateDataError";
    private final String DELETE_USER_ERROR = "deleteUserError";
    private final String MY_USER_REFERENCE = "myUser";
    private final String USER_CREATION_DATE = "dateCreate";
    private final String ANONYMIZE_USER_ERROR = "anonymizeUserError";

    private final String EXISTS_PARTNER = "existsPartner";
    private final String IS_NEW_EVALUATION = "isNewEvaluation";
    private final String REGISTERED_PARTNER = "registeredPartner";
    private final String DELETE_PARTNER_ERROR = "deletePartnerError";
    private final String MY_PARTNER_REFERENCE = "myPartner";
    private final String UPDATE_PARTNER_ERROR = "updatePartnerError";

    private boolean deleteUserError = false;
    private boolean anonymizeUserError = false;
    private boolean triedUpdateUserData = false;
    private boolean updateUserDataError = false;

    private boolean updatePartnerError = false;
    private boolean deletePartnerError = false;

    private Usuario temporaryUser = null;
    private Parceiro temporaryPartner = null;

    // ADMIN USER AREA
    @GetMapping("/user/administrator-area")
    public String getOptionUserAdminsitratorPage() {
        return FoldersName.ADMIN_FOLDER + "/area-administrador";
    }

    @GetMapping("/user/read-all")
    public String getReadAllUsersPage(final Model model) {
        final List<Usuario> userList = (List<Usuario>) userWebService.readAll();

        boolean existsUsers = true;
        if (userList == null || userList.isEmpty()) {
            existsUsers = false;
        }

        model.addAttribute(EXISTS_USERS, existsUsers);
        model.addAttribute(REGISTERED_USERS, userList);
        return FoldersName.ADMIN_USER_FOLDER + "/usuarios_list";
    }

    @GetMapping("/user/admin-profile/{id}")
    public String getUserProfilePage(@PathVariable final long id, final Model model) {
        final Usuario user = (Usuario) userWebService.readById(id);
        model.addAttribute(MY_USER_REFERENCE, user);
        model.addAttribute(USER_ID, user.getId());
        model.addAttribute(USER_CREATION_DATE, userWebService.getCreationDateAndTime(user.getDataHora()));

        model.addAttribute(DELETE_USER_ERROR, deleteUserError);
        if (deleteUserError) {
            deleteUserError = false;
        }
        model.addAttribute(ANONYMIZE_USER_ERROR, anonymizeUserError);
        if (anonymizeUserError) {
            deleteUserError = false;
        }

        model.addAttribute(DATA_UPDATE_ERROR, updateUserDataError);
        if (updateUserDataError) {
            updateUserDataError = false;
        }
        return FoldersName.ADMIN_USER_FOLDER + "/perfil_usuario";
    }

    @GetMapping("/user/admin-edit/{id}")
    public String getUserEditPage(@PathVariable final int id, final Model model) {
        try {
            final Usuario user = (Usuario) userWebService.readById(id);
            if (user != null) {
                triedUpdateUserData = false;
                temporaryUser = null;
                model.addAttribute(user);
                model.addAttribute(USER_ID, user.getId());
                model.addAttribute(DATA_UPDATE_ERROR, updateUserDataError);
                updateUserDataError = false;
                return FoldersName.ADMIN_USER_FOLDER + "/editar_perfil_usuario";
            }

            if (triedUpdateUserData && temporaryUser != null) {
                triedUpdateUserData = false;
                model.addAttribute(temporaryUser);
                model.addAttribute(USER_ID, temporaryUser.getId());
                temporaryUser = null;

                model.addAttribute(DATA_UPDATE_ERROR, updateUserDataError);
                updateUserDataError = false;
                return FoldersName.ADMIN_USER_FOLDER + "/editar_perfil_usuario";
            }

            triedUpdateUserData = false;
            updateUserDataError = false;
            return "redirect:/not-found";

        } catch (final Exception ex) {
            triedUpdateUserData = false;
            updateUserDataError = false;
            return "redirect:/not-found";
        }
    }

    @PostMapping("/user/admin-update-user-data")
    public String updateUserData(final Usuario user, final Model model) {
        triedUpdateUserData = true;
        temporaryUser = user;

        updateUserDataError = !userWebService.update(user);
        if (updateUserDataError) {
            return "redirect:/user/admin-edit/" + user.getId();
        }
        triedUpdateUserData = false;
        return "redirect:/user/admin-profile/" + user.getId();
    }

    @GetMapping("/user/admin-delete/{id}")
    public String deleteUserAccount(@PathVariable final long id) {
        deleteUserError = !userWebService.delete(id);
        if (deleteUserError) {
            return "redirect:/user/admin-profile/" + id;
        }
        return "redirect:/user/read-all";
    }

    @GetMapping("/user/admin-anonymize-user/{id}")
    public String anonymizeUser(@PathVariable final long id) {
        anonymizeUserError = !new UserWebServiceImpl().anonymizeUser(id);
        return "redirect:/user/admin-profile/" + id;
    }

    @GetMapping("/user/remove-user-anonymization/{id}")
    public String removeUserAnonymization(@PathVariable final long id) {
        updateUserDataError = !new UserWebServiceImpl().removeUserAnonymization(id);
        return "redirect:/user/admin-profile/" + id;
    }

    @GetMapping("/user/give-admin-permission/{userID}")
    public String giveAdminPermission(@PathVariable final long userID) {
        final Usuario user = (Usuario) userWebService.readById(userID);
        if (user == null) {
            updateUserDataError = false;
            return "redirect:/user/admin-profile/" + userID;
        }

        user.setAdministrador(true);
        updateUserDataError = !userWebService.update(user);
        return "redirect:/user/admin-profile/" + user.getId();
    }

    @GetMapping("/user/remove-admin-permission/{userID}")
    public String removeAdminPermission(@PathVariable final long userID) {
        final Usuario user = (Usuario) userWebService.readById(userID);
        if (user == null) {
            updateUserDataError = false;
            return "redirect:/user/admin-profile/" + userID;
        }

        user.setAdministrador(false);
        updateUserDataError = !userWebService.update(user);
        return "redirect:/user/admin-profile/" + user.getId();
    }

    //ADMIN PARTNER AREA
    @GetMapping("/partner/request-register-list")
    public String getRequestRegisterListPage(final Model model) {
        final Map<String, Object> map = new HashMap<>();
        map.put(Usuario.USER_TABLE.IS_ANONYMOUS_COLUMN, false);
        map.put(Parceiro.PARTNER_TABLE.SITUATION_COLUMN, Parceiro.SITUATIONS.REQUESTED);
        final List<Parceiro> partnerList = (List<Parceiro>) partnerWebService.readByCriteria(map);

        boolean existsParner = true;
        if (partnerList == null || partnerList.isEmpty()) {
            existsParner = false;
        }

        model.addAttribute(EXISTS_PARTNER, existsParner);
        model.addAttribute(REGISTERED_PARTNER, partnerList);
        return FoldersName.ADMIN_PARTNER_FOLDER + "/solicitacoes_registro_consultor";
    }

    @GetMapping("/partner/evaluate-registration-request/{id}")
    public String getRequestRegisterListPage(@PathVariable final long id, final Model model) {
        final Parceiro partner = (Parceiro) partnerWebService.readById(id);
        if (partner == null) {
            return "redirect:/not-found";
        }
        model.addAttribute(MY_PARTNER_REFERENCE, partner);
        model.addAttribute(UPDATE_PARTNER_ERROR, updatePartnerError);
        if (updatePartnerError) {
            updatePartnerError = false;
        }

        model.addAttribute(IS_NEW_EVALUATION, false);
        return FoldersName.ADMIN_PARTNER_FOLDER + "/avaliar_registro_consultor";
    }

    @GetMapping("/partner/approve-registration-request/{id}")
    public String getApproveRegisterListPage(@PathVariable final long id) {
        final Parceiro partner = (Parceiro) partnerWebService.readById(id);
        if (partner == null) {
            updatePartnerError = true;
            return "redirect:/partner/evaluate-registration-request/" + id;
        }

        partner.setSituacao(Parceiro.SITUATIONS.APPROVED);
        updatePartnerError = !partnerWebService.update(partner);
        if (updatePartnerError) {
            return "redirect:/partner/evaluate-registration-request/" + id;
        }
        return "redirect:/partner/request-register-list";
    }

    @GetMapping("/partner/reprove-registration-request/{id}")
    public String getReproveRegisterListPage(@PathVariable final long id) {
        final Parceiro partner = (Parceiro) partnerWebService.readById(id);
        if (partner == null) {
            updatePartnerError = true;
            return "redirect:/partner/evaluate-registration-request/" + id;
        }

        partner.setSituacao(Parceiro.SITUATIONS.REPROVED);
        updatePartnerError = !partnerWebService.update(partner);
        if (updatePartnerError) {
            return "redirect:/partner/evaluate-registration-request/" + id;
        }

        return "redirect:/partner/request-register-list";
    }

    @GetMapping("/partner/reproved-register-list")
    public String getReproveRegisterListPage(final Model model) {
        final Map<String, Object> map = new HashMap<>();

        map.put(Usuario.USER_TABLE.IS_ANONYMOUS_COLUMN, false);
        map.put(Parceiro.PARTNER_TABLE.SITUATION_COLUMN, Parceiro.SITUATIONS.REPROVED);
        final List<Parceiro> partnerList = (List<Parceiro>) partnerWebService.readByCriteria(map);

        boolean existsParner = true;
        if (partnerList == null || partnerList.isEmpty()) {
            existsParner = false;
        }

        model.addAttribute(EXISTS_PARTNER, existsParner);
        model.addAttribute(REGISTERED_PARTNER, partnerList);
        return FoldersName.ADMIN_PARTNER_FOLDER + "/solicitacoes_consultor_reprovadas";
    }

    @GetMapping("/partner/new-evaluate-registration-request/{id}")
    public String getNewRequestRegisterListPage(@PathVariable final long id, final Model model) {
        final Parceiro partner = (Parceiro) partnerWebService.readById(id);
        if (partner == null) {
            return "redirect:/not-found";
        }
        model.addAttribute(MY_PARTNER_REFERENCE, partner);
        model.addAttribute(UPDATE_PARTNER_ERROR, updatePartnerError);
        if (updatePartnerError) {
            updatePartnerError = false;
        }

        model.addAttribute(IS_NEW_EVALUATION, true);
        return FoldersName.ADMIN_PARTNER_FOLDER + "/avaliar_registro_consultor";
    }

    @GetMapping("/partner/new-approve-registration-request/{id}")
    public String getNewApproveRegisterListPage(@PathVariable final long id) {
        final Parceiro partner = (Parceiro) partnerWebService.readById(id);
        if (partner == null) {
            updatePartnerError = true;
            return "redirect:/partner/new-evaluate-registration-request/" + id;
        }

        partner.setSituacao(Parceiro.SITUATIONS.APPROVED);
        updatePartnerError = !partnerWebService.update(partner);
        if (updatePartnerError) {
            return "redirect:/partner/new-evaluate-registration-request/" + id;
        }
        return "redirect:/partner/reproved-register-list";
    }

    @GetMapping("/partner/admin-list")
    public String getPartnerListPage(final Model model) {
        final List<Parceiro> partnerList = new ArrayList<>();

        final Map<String, Object> map = new HashMap<>();
        map.put(Parceiro.PARTNER_TABLE.SITUATION_COLUMN, Parceiro.SITUATIONS.APPROVED);
        final List<Parceiro> approvedPartnerList = (List<Parceiro>) partnerWebService.readByCriteria(map);
        if (approvedPartnerList != null && !approvedPartnerList.isEmpty()) {
            approvedPartnerList.stream().forEach(partner -> partnerList.add(partner));
        }

        map.replace(Parceiro.PARTNER_TABLE.SITUATION_COLUMN, Parceiro.SITUATIONS.EXCLUDED);
        final List<Parceiro> excludedPartnerList = (List<Parceiro>) partnerWebService.readByCriteria(map);
        if (excludedPartnerList != null && !excludedPartnerList.isEmpty()) {
            excludedPartnerList.stream().forEach(partner -> partnerList.add(partner));
        }

        boolean existsParner = true;
        if (partnerList == null || partnerList.isEmpty()) {
            existsParner = false;
        }

        model.addAttribute(EXISTS_PARTNER, existsParner);
        model.addAttribute(REGISTERED_PARTNER, partnerList);
        return FoldersName.ADMIN_PARTNER_FOLDER + "/consultores_aprovados";
    }

    @GetMapping("/partner/admin-detail/{id}")
    public String getPartnerDetailPage(@PathVariable final long id, final Model model) {
        final Map<String, Long> map = new HashMap<>();
        map.put(Parceiro.PARTNER_TABLE.ID_USER_COLUMN, id);
        final List<Parceiro> partnerList = (List<Parceiro>) partnerWebService.readByCriteria(map);
        if (partnerList == null || partnerList.isEmpty() || partnerList.size() > 1) {
            return "redirect:/not-found";
        }

        model.addAttribute(UPDATE_PARTNER_ERROR, updatePartnerError);
        if (updatePartnerError) {
            updatePartnerError = false;
        }

        model.addAttribute(DELETE_PARTNER_ERROR, deletePartnerError);
        if (deletePartnerError) {
            deletePartnerError = false;
        }

        model.addAttribute(MY_PARTNER_REFERENCE, partnerList.get(0));
        return FoldersName.ADMIN_PARTNER_FOLDER + "/consultor_detail";
    }

    @GetMapping("/partner/edit-partner-data/{id}")
    public String getEditPartnerDataPage(@PathVariable final long id, final Model model) {
        Parceiro partner = (Parceiro) partnerWebService.readById(id);
        if (partner == null) {
            return "redirect:/not-found";
        }

        model.addAttribute(UPDATE_PARTNER_ERROR, updatePartnerError);
        if (updatePartnerError) {
            updatePartnerError = false;
        }

        if (temporaryPartner != null) {
            partner = temporaryPartner;
            temporaryPartner = null;
        }
        model.addAttribute(MY_PARTNER_REFERENCE, partner);
        model.addAttribute(USER_ID, partner.getIdUsuario());
        return FoldersName.ADMIN_PARTNER_FOLDER + "/consultor_edit";
    }

    @PostMapping("/partner/update-partner-data")
    public String updatePartnerDataPage(final Model model, final Parceiro partner) {
        updatePartnerError = !partnerWebService.update(partner);
        if (updatePartnerError) {
            temporaryPartner = partner;
            return "redirect:/partner/edit-partner-data/" + partner.getId();
        }

        return "redirect:/partner/admin-detail/" + partner.getIdUsuario();
    }

    @GetMapping("/partner/deactivate-partner/{userId}")
    public String deactivatePartner(@PathVariable final long userId) {
        final Map<String, Long> map = new HashMap<>();
        map.put(Parceiro.PARTNER_TABLE.ID_USER_COLUMN, userId);
        final List<Parceiro> partnerList = (List<Parceiro>) partnerWebService.readByCriteria(map);
        if (partnerList == null || partnerList.isEmpty() || partnerList.size() > 1) {
            updatePartnerError = true;
            return "redirect:/partner/admin-detail/" + userId;
        }

        final Parceiro partner = partnerList.get(0);
        partner.setSituacao(Parceiro.SITUATIONS.EXCLUDED);
        updatePartnerError = !partnerWebService.update(partner);
        return "redirect:/partner/admin-detail/" + userId;
    }

    @GetMapping("/partner/reactivate-partner/{userId}")
    public String reactivatePartner(@PathVariable final long userId) {
        final Map<String, Long> map = new HashMap<>();
        map.put(Parceiro.PARTNER_TABLE.ID_USER_COLUMN, userId);
        final List<Parceiro> partnerList = (List<Parceiro>) partnerWebService.readByCriteria(map);
        if (partnerList == null || partnerList.isEmpty() || partnerList.size() > 1) {
            updatePartnerError = true;
            return "redirect:/partner/admin-detail/" + userId;
        }

        final Parceiro partner = partnerList.get(0);
        partner.setSituacao(Parceiro.SITUATIONS.APPROVED);
        updatePartnerError = !partnerWebService.update(partner);
        return "redirect:/partner/admin-detail/" + userId;
    }

    @GetMapping("/partner/delte-partner/{userId}")
    public String deletePartner(@PathVariable final long userId) {
        final Map<String, Long> map = new HashMap<>();
        map.put(Parceiro.PARTNER_TABLE.ID_USER_COLUMN, userId);
        final List<Parceiro> partnerList = (List<Parceiro>) partnerWebService.readByCriteria(map);
        if (partnerList == null || partnerList.isEmpty() || partnerList.size() > 1) {
            deletePartnerError = true;
            return "redirect:/partner/admin-detail/" + userId;
        }

        deletePartnerError = !partnerWebService.delete(partnerList.get(0).getId());
        if (deletePartnerError) {
            return "redirect:/partner/admin-detail/" + userId;
        }
        return "redirect:/partner/admin-list";
    }
}
