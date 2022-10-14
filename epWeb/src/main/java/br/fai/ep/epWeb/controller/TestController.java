package br.fai.ep.epWeb.controller;

import br.fai.ep.epEntities.DTO.QuestionDto;
import br.fai.ep.epEntities.Usuario;
import br.fai.ep.epWeb.helper.FoldersName;
import br.fai.ep.epWeb.helper.Questionnaire;
import br.fai.ep.epWeb.security.provider.EpAuthenticationProvider;
import br.fai.ep.epWeb.service.impl.QuestWebServiceImpl;
import br.fai.ep.epWeb.service.impl.TestWebServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class TestController {
    private final TestWebServiceImpl testWebService = new TestWebServiceImpl();
    private final QuestWebServiceImpl questWebService = new QuestWebServiceImpl();
    private final EpAuthenticationProvider epAuthenticationProvider = new EpAuthenticationProvider();

    private final String EXISTS_QUESTIONS = "existsQuestions";
    private final String QUESTIONS_LIST = "myQuestionnaire";
    private final String SAVE_TEST_ERROR = "saveTestError";

    private boolean saveTestError;

    private Questionnaire temporaryQuestionary = null;

    @GetMapping("/knowledge-test/select-level")
    public String getQuestionnairePage() {
        return FoldersName.KNOWLEDGE_TEST + "/select_level";
    }

    @GetMapping("/knowledge-test/test/{level}")
    public String getQuestionnairePage(@PathVariable final int level, final Model model) {
        final Map<String, Integer> criteria = new HashMap<>();
        criteria.put(br.fai.ep.epEntities.Questao.QUESTION_TABLE.LEVEL_COLUMN, level);
        final List<QuestionDto> questionList = questWebService.readByDtoCriteria(criteria);

        boolean existsQuestions = false;
        if (questionList != null && !questionList.isEmpty()) {
            existsQuestions = true;

            final Questionnaire questionnaire = new Questionnaire();
            questionList.stream().forEach(questionnaire::addQuestion);
            model.addAttribute(QUESTIONS_LIST, questionnaire);
        }

        model.addAttribute(EXISTS_QUESTIONS, existsQuestions);
        return FoldersName.KNOWLEDGE_TEST + "/knowledge_test";
    }

    @PostMapping("/knowledge-test/result-test")
    public String getResultKnowledgeTestPage(Questionnaire questionnaire, final Model model) {
        model.addAttribute(SAVE_TEST_ERROR, saveTestError);
        if (questionnaire != null && saveTestError) {
            questionnaire = temporaryQuestionary;
            temporaryQuestionary = null;
            saveTestError = false;
        }

        model.addAttribute(QUESTIONS_LIST, questionnaire);
        return FoldersName.KNOWLEDGE_TEST + "/result_test";
    }

    @PostMapping("/knowledge-test/save-result-test")
    public String getSaveResultKnowledgeTestPage(final Questionnaire questionnaire, final Model model) {
        final Usuario user = epAuthenticationProvider.getAuthenticatedUser();
        if (user == null) {
            saveTestError = true;
            temporaryQuestionary = questionnaire;
        }
        questionnaire.getQuestions().stream().forEach(question -> question.setUserId(user.getId()));
        final long newTestId = testWebService.create(questionnaire.getQuestions());
        System.out.println(newTestId);
        if (newTestId == -1) {
            model.addAttribute(QUESTIONS_LIST, questionnaire);
            return FoldersName.KNOWLEDGE_TEST + "/result_test";
        }
        return "redirect:/knowledge-test/select-level";
    }
}