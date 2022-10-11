package br.fai.ep.epWeb.controller;

import br.fai.ep.epEntities.Questao;
import br.fai.ep.epWeb.helper.FoldersName;
import br.fai.ep.epWeb.service.impl.QuestWebServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class QuestController {

    QuestWebServiceImpl service = new QuestWebServiceImpl();

    public final String EXISTS_QUESTS = "existsQuest";
    public final String MY_QUEST_REFERENCE = "myQuest";
    public final String QUEST_LIST_REFERENCE = "questList";
    public final String QUEST_REGISTER_ERROR = "questRegisterError";

    private boolean questRegisterError;

    private Questao temporaryQuest = null;

    @GetMapping("/question/list")
    public String getQuestListrPage(final Model model) {
        final List<Questao> questList = (List<Questao>) service.readAll();
        final boolean existQuests = (questList == null || questList.isEmpty()) ? false : true;
        model.addAttribute(EXISTS_QUESTS, existQuests);
        model.addAttribute(QUEST_LIST_REFERENCE, questList);

        return FoldersName.ADMIN_QUEST_FOLDER + "/quest_list";
    }

    @GetMapping("/question/register")
    public String getQuestRegisterPage(final Model model, Questao quest) {
        model.addAttribute(QUEST_REGISTER_ERROR, questRegisterError);
        if (questRegisterError) {
            questRegisterError = false;
        }

        if (temporaryQuest != null) {
            quest = temporaryQuest;
            temporaryQuest = null;
        }
        model.addAttribute(MY_QUEST_REFERENCE, quest);
        return FoldersName.ADMIN_QUEST_FOLDER + "/quest_register";
    }

    @PostMapping("/question/create-quest")
    public String createQuest(final Questao quest) {
        final long newQuestId = service.create(quest);
        if (newQuestId == -1) {
            questRegisterError = true;
            temporaryQuest = quest;
            return "redirect:/question/register";
        }

        return "redirect:/question/list";
    }
}