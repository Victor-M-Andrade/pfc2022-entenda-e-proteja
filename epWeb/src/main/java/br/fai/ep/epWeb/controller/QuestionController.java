package br.fai.ep.epWeb.controller;

import br.fai.ep.epEntities.Questao;
import br.fai.ep.epWeb.helper.FoldersName;
import br.fai.ep.epWeb.service.impl.QuestWebServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class QuestionController {

    QuestWebServiceImpl service = new QuestWebServiceImpl();

    public final String EXISTS_QUESTS = "existsQuest";
    public final String MY_QUEST_REFERENCE = "myQuest";
    public final String QUEST_LIST_REFERENCE = "questList";
    public final String QUEST_REGISTER_ERROR = "questRegisterError";
    public final String DELETE_QUESTION_ERROR = "deleteQuestionError";
    public final String UPDATE_QUESTION_ERROR = "updateQuestionError";

    private boolean questRegisterError;
    private boolean deleteQuestionError;
    private boolean updateQuestionError;

    private Questao temporaryQuest = null;

    @GetMapping("/question/list")
    public String getQuestListrPage(final Model model) {
        final List<Questao> questList = (List<Questao>) service.readAll();
        final boolean existQuests = (questList == null || questList.isEmpty()) ? false : true;
        model.addAttribute(EXISTS_QUESTS, existQuests);
        model.addAttribute(QUEST_LIST_REFERENCE, questList);

        return FoldersName.ADMIN_QUEST_FOLDER + "/question_list";
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
        return FoldersName.ADMIN_QUEST_FOLDER + "/question_register";
    }

    @PostMapping("/question/create-quest")
    public String createQuest(final Questao question) {
        final long newQuestId = service.create(question);
        if (newQuestId == -1) {
            questRegisterError = true;
            temporaryQuest = question;
            return "redirect:/question/register";
        }

        return "redirect:/question/detail/" + newQuestId;
    }

    @GetMapping("/question/detail/{id}")
    public String getQuestionDetailPage(@PathVariable final long id, final Model model) {
        final Questao question = (Questao) service.readById(id);
        if (question == null) {
            return "redirect:/not-found";
        }

        model.addAttribute(DELETE_QUESTION_ERROR, deleteQuestionError);
        deleteQuestionError = false;

        model.addAttribute(MY_QUEST_REFERENCE, question);
        return FoldersName.ADMIN_QUEST_FOLDER + "/question_detail";
    }

    @GetMapping("/question/edit-question/{id}")
    public String getQuestionEditPage(@PathVariable final long id, final Model model) {
        final Questao question = (temporaryQuest != null) ? temporaryQuest : (Questao) service.readById(id);
        if (question == null) {
            return "redirect:/not-found";
        }

        model.addAttribute(UPDATE_QUESTION_ERROR, updateQuestionError);
        updateQuestionError = false;

        model.addAttribute(MY_QUEST_REFERENCE, question);
        return FoldersName.ADMIN_QUEST_FOLDER + "/question_edit";
    }

    @PostMapping("/question/update-question")
    public String updateQuestion(final Questao question) {
        updateQuestionError = !service.update(question);
        if (updateQuestionError) {
            temporaryQuest = question;
        }
        final String redirectPage = updateQuestionError ? "/question/edit-question/" : "/question/detail/";
        return "redirect:" + redirectPage + question.getId();
    }

    @GetMapping("/question/delete-question/{id}")
    public String deleteQuestion(@PathVariable final long id) {
        deleteQuestionError = !service.delete(id);
        final String redirectPage = deleteQuestionError ? "/question/detail/" + id : "/question/list";
        return "redirect:" + redirectPage;
    }
}