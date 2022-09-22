package br.fai.ep.epWeb.controller;

import br.fai.ep.epEntities.Parceiro;
import br.fai.ep.epEntities.Usuario;
import br.fai.ep.epWeb.service.WebServiceInterface;
import br.fai.ep.epWeb.service.impl.PartnerWebServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class PartnerController {
    private final WebServiceInterface service = new PartnerWebServiceImpl();

    private final String EXISTS_PARTNER = "existsPartner";
    private final String REGISTERED_PARTNER = "registeredPartner";
    private final String MY_PARTNER_REFERENCE = "myPartner";


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
}