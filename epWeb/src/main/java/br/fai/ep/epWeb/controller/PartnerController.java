package br.fai.ep.epWeb.controller;

import br.fai.ep.epEntities.Parceiro;
import br.fai.ep.epEntities.Usuario;
import br.fai.ep.epWeb.service.WebServiceInterface;
import br.fai.ep.epWeb.service.impl.PartnerWebServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class PartnerController {
    private final WebServiceInterface service = new PartnerWebServiceImpl();

    private final String USER_ID = "userId";
    private final String EXISTS_PARTNER = "existsPartner";
    private final String REGISTERED_PARTNER = "registeredPartner";
    private final String MY_PARTNER_REFERENCE = "myPartner";
    private final String UPDATE_PARTNER_ERROR = "updatePartnerError";
    private final String ALREADY_REGISTERED_CNPJ = "alreadyRegisteredCnpj";
    private final String REGISTRATION_REQUEST_PROBLEMS = "registrationRequestProblems";

    private boolean alreadyRegisteredCnpj = false;
    private boolean updatePartnerError = false;
    private boolean registrationRequestProblems = false;

    private Parceiro temporaryPartner = null;

    @GetMapping("/partner/list")
    public String getPartnerListPage(final Model model) {
        final Map<String, Boolean> map = new HashMap<>();
        map.put(Usuario.USER_TABLE.IS_ANONYMOUS_COLUMN, false);
        final List<Parceiro> userList = (List<Parceiro>) service.readByCriteria(map);

        boolean existsParner = true;
        if (userList == null || userList.isEmpty()) {
            existsParner = false;
        }

        model.addAttribute(EXISTS_PARTNER, existsParner);
        model.addAttribute(REGISTERED_PARTNER, userList);
        return "parceiro/consultoria_list";
    }

    @GetMapping("/partner/detail/{id}")
    public String getPartnerDetailPage(@PathVariable final long id, final Model model) {
        final Parceiro partner = (Parceiro) service.readById(id);
        model.addAttribute(MY_PARTNER_REFERENCE, partner);

        return "parceiro/consultoria_info";
    }

    @GetMapping("/partner/register/{id}")
    public String getRegisterPartnerPage(@PathVariable final long id, final Model model, Parceiro partner) {
        model.addAttribute(USER_ID, id);

        model.addAttribute(ALREADY_REGISTERED_CNPJ, alreadyRegisteredCnpj);
        if (alreadyRegisteredCnpj) {
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
        partner.setIdUsuario(id);
        model.addAttribute(MY_PARTNER_REFERENCE, partner);
        return "parceiro/register_parceiro";
    }

    @PostMapping("/partner/request-register")
    public String requestPartnerRegistration(final Parceiro partner) {
        final long newPartnerId = service.create(partner);
        if (newPartnerId == -1) {
            registrationRequestProblems = true;
            temporaryPartner = partner;
            return "redirect:/partner/register/" + partner.getIdUsuario();
        } else if (newPartnerId == -2) {
            alreadyRegisteredCnpj = true;
            temporaryPartner = partner;
            return "redirect:/partner/register/" + partner.getIdUsuario();
        }

        return "redirect:/partner/detail/" + newPartnerId;
    }

    @GetMapping("/partner/my-data-as-partner/{id}")
    public String getMyDataAsPartnerPage(@PathVariable final long id, final Model model) {
        final Map<String, Long> map = new HashMap<>();
        map.put(Parceiro.PARTNER_TABLE.ID_USER_COLUMN, id);
        final List<Parceiro> partnerList = (List<Parceiro>) service.readByCriteria(map);
        if (partnerList == null || partnerList.isEmpty() || partnerList.size() > 1) {
            return "redirect:/not-found";
        }

        model.addAttribute(MY_PARTNER_REFERENCE, partnerList.get(0));
        model.addAttribute(USER_ID, partnerList.get(0).getIdUsuario());
        return "parceiro/perfil_consultor";
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
        model.addAttribute(USER_ID, partner.getIdUsuario());
        return "parceiro/editar_parceiro";
    }

    @PostMapping("/partner/update-my-data-as-partner")
    public String getEditMyDataAsPartnerPage(final Model model, final Parceiro partner) {
        updatePartnerError = !service.update(partner);
        if (updatePartnerError) {
            temporaryPartner = partner;
            return "redirect:/partner/edit-my-data-as-partner/" + partner.getId();
        }

        return "redirect:/partner/my-data-as-partner/" + partner.getIdUsuario();
    }
}