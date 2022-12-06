package br.fai.ep.epWeb.controller;

import br.fai.ep.epEntities.Parceiro;
import br.fai.ep.epEntities.Parceiro.REGISTER_ERROR;
import br.fai.ep.epEntities.Usuario;
import br.fai.ep.epWeb.helper.FoldersName;
import br.fai.ep.epWeb.security.provider.EpAuthenticationProvider;
import br.fai.ep.epWeb.service.BaseWebService;
import br.fai.ep.epWeb.service.FileService;
import br.fai.ep.epWeb.service.impl.PartnerWebServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class PartnerController {

    @Autowired
    private FileService awsFileService;

    private final PartnerWebServiceImpl service = new PartnerWebServiceImpl();
    private final EpAuthenticationProvider epAuthenticationProvider = new EpAuthenticationProvider();

    private final String EXISTS_PARTNER = "existsPartner";
    private final String LOAD_IMAGE_ERROR = "loadImageError";
    private final String REGISTERED_PARTNER = "registeredPartner";
    private final String MY_PARTNER_REFERENCE = "myPartner";
    private final String UPDATE_PARTNER_ERROR = "updatePartnerError";
    private final String DELETE_PARTNER_ERROR = "deletePartnerError";
    private final String ALREADY_REGISTERED_CNPJ = "alreadyRegisteredCnpj";
    private final String ALREADY_REGISTERED_PARTNER = "alreadyRegisteredPartner";
    private final String REGISTRATION_REQUEST_PROBLEMS = "registrationRequestProblems";

    private boolean updatePartnerError = false;
    private boolean deletePartnerError = false;
    private boolean alreadyRegisteredCnpj = false;
    private boolean alreadyRegisteredPartner = false;
    private boolean registrationRequestProblems = false;

    private Parceiro temporaryPartner = null;

    @GetMapping("/partner/list")
    public String getPartnerListPage(final Model model) {
        final Map<String, Object> map = new HashMap<>();
        map.put(Usuario.USER_TABLE.IS_ANONYMOUS_COLUMN, false);
        map.put(Parceiro.PARTNER_TABLE.SITUATION_COLUMN, Parceiro.SITUATIONS.APPROVED);
        final List<Parceiro> partnerList = (List<Parceiro>) service.readByCriteria(map);

        boolean existsParner = true;
        if (partnerList == null || partnerList.isEmpty()) {
            existsParner = false;
        }

        model.addAttribute(EXISTS_PARTNER, existsParner);
        model.addAttribute(REGISTERED_PARTNER, partnerList);
        return FoldersName.PARTNER_FOLDER + "/consultoria_list";
    }

    @GetMapping("/partner/new-registration-request/{id}")
    public String getNewRegisterListPage(@PathVariable final long id) {
        final Parceiro partner = (Parceiro) service.readById(id);
        if (partner == null) {
            updatePartnerError = true;
            return "redirect:/partner/evaluate-registration-request/" + id;
        }

        partner.setSituacao(Parceiro.SITUATIONS.REQUESTED);
        updatePartnerError = !service.update(partner);
        return "redirect:/partner/my-data-as-partner";
    }

    @GetMapping("/partner/detail/{id}")
    public String getPartnerDetailPage(@PathVariable final long id, final Model model) {
        final Parceiro partner = (Parceiro) service.readPartnerDetail(id);
        if (partner == null) {
            return "redirect:/not-found";
        }

        model.addAttribute(MY_PARTNER_REFERENCE, partner);
        return FoldersName.PARTNER_FOLDER + "/consultoria_info";
    }

    @GetMapping("/partner/register")
    public String getRegisterPartnerPage(final Model model, Parceiro partner) {
        final Usuario authenticatedUser = epAuthenticationProvider.getAuthenticatedUser();
        if (authenticatedUser == null) {
            return "redirect:/not-found";
        }

        model.addAttribute(ALREADY_REGISTERED_CNPJ, alreadyRegisteredCnpj);
        if (alreadyRegisteredCnpj) {
            alreadyRegisteredCnpj = false;
        }

        model.addAttribute(ALREADY_REGISTERED_PARTNER, alreadyRegisteredPartner);
        if (alreadyRegisteredPartner) {
            alreadyRegisteredCnpj = false;
        }

        model.addAttribute(REGISTRATION_REQUEST_PROBLEMS, registrationRequestProblems);
        if (registrationRequestProblems) {
            registrationRequestProblems = false;
        }

        if (temporaryPartner != null) {
            partner = temporaryPartner;
            temporaryPartner = null;
        }
        partner.setIdUsuario(authenticatedUser.getId());
        model.addAttribute(MY_PARTNER_REFERENCE, partner);
        return FoldersName.PARTNER_FOLDER + "/register_parceiro";
    }

    @PostMapping("/partner/request-register")
    public String requestPartnerRegistration(final Parceiro partner) {
        final long newPartnerId = service.create(partner);
        if (newPartnerId == REGISTER_ERROR.REGISTRATION_REQUEST_PROBLEMNS.getError()) {
            registrationRequestProblems = true;
        } else if (newPartnerId == REGISTER_ERROR.ALREADY_REGISTERED_CNPJ.getError()) {
            alreadyRegisteredCnpj = true;
        } else if (newPartnerId == REGISTER_ERROR.ALREADY_REGISTERED_PARTNER.getError()) {
            alreadyRegisteredPartner = true;
        }

        if (newPartnerId < 0) {
            temporaryPartner = partner;
            return "redirect:/partner/register";
        }

        return "redirect:/partner/my-data-as-partner";
    }

    @GetMapping("/partner/my-data-as-partner")
    public String getMyDataAsPartnerPage(final Model model) {
        final Usuario authenticatedUser = epAuthenticationProvider.getAuthenticatedUser();
        if (authenticatedUser == null) {
            return "redirect:/not-found";
        }

        final Map<String, Long> map = new HashMap<>();
        map.put(Parceiro.PARTNER_TABLE.ID_USER_COLUMN, authenticatedUser.getId());
        final List<Parceiro> partnerList = (List<Parceiro>) service.readByCriteria(map);
        if (partnerList == null || partnerList.isEmpty() || partnerList.size() > 1) {
            return "redirect:/not-found";
        }

        final Parceiro partner = partnerList.get(0);
        if (!partner.getPathImageProfile().equalsIgnoreCase(Usuario.DEFAULT_IMAGE_PATH)) {
            final byte[] byteImage = awsFileService.downloadFile(partner.getPathImageProfile().substring(1));
            model.addAttribute(LOAD_IMAGE_ERROR, (byteImage == null || byteImage.length == 0));
        }

        model.addAttribute(DELETE_PARTNER_ERROR, deletePartnerError);
        if (deletePartnerError) {
            deletePartnerError = false;
        }

        model.addAttribute(UPDATE_PARTNER_ERROR, updatePartnerError);
        if (updatePartnerError) {
            updatePartnerError = false;
        }

        model.addAttribute(MY_PARTNER_REFERENCE, partner);
        return FoldersName.PARTNER_FOLDER + "/perfil_consultor";
    }

    @GetMapping("/partner/edit-my-data-as-partner/{id}")
    public String getEditMyDataAsPartnerPage(@PathVariable final long id, final Model model) {
        Parceiro partner = (Parceiro) service.readById(id);
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
        return FoldersName.PARTNER_FOLDER + "/editar_parceiro";
    }

    @PostMapping("/partner/update-my-data-as-partner")
    public String updateMyDataAsPartnerPage(@RequestParam("partnerProfile") final MultipartFile file, final Model model, final Parceiro partner) {
        temporaryPartner = partner;
        if (file.isEmpty()) {
            updatePartnerError = !service.update(partner);
            if (updatePartnerError) {
                temporaryPartner = partner;
                return "redirect:/partner/edit-my-data-as-partner/" + partner.getId();
            }
            temporaryPartner = null;
            return "redirect:/partner/my-data-as-partner";
        }
        final String newNameFile = service.buildNameNewFile(partner);
        final String nameFileWithExtension = service.prepareNameWithExtension(file.getOriginalFilename(), newNameFile);
        if (nameFileWithExtension == null) {
            updatePartnerError = !service.update(partner);
            if (updatePartnerError) {
                temporaryPartner = partner;
                return "redirect:/partner/edit-my-data-as-partner/" + partner.getId();
            }
            temporaryPartner = null;
            return "redirect:/partner/my-data-as-partner";
        }

        final String newKeyFile = BaseWebService.PATH_IMAGENS_PARTNER + nameFileWithExtension;
        final String pathImage = awsFileService.uploadFile(file, newKeyFile, partner.getPathImagePartner().substring(1));
        if (pathImage != null) {
            partner.setPathImagePartner(pathImage);
        }

        updatePartnerError = !service.update(partner);
        if (updatePartnerError) {
            temporaryPartner = partner;
            return "redirect:/partner/edit-my-data-as-partner/" + partner.getId();
        }
        temporaryPartner = null;
        return "redirect:/partner/my-data-as-partner";
    }

    @GetMapping("/partner/delete-my-data-as-partner/{id}")
    public String deleteMyDataAsPartner(@PathVariable final long id) {
        deletePartnerError = !service.delete(id);
        if (deletePartnerError) {
            return "redirect:/partner/my-data-as-partner";
        }
        return "redirect:/user/profile";
    }
}