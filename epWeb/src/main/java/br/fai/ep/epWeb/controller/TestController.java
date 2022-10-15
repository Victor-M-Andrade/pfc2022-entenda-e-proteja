package br.fai.ep.epWeb.controller;

import br.fai.ep.epEntities.DTO.QuestionDto;
import br.fai.ep.epEntities.Teste;
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
import java.util.stream.Collectors;

@Controller
public class TestController {
    private final TestWebServiceImpl testWebService = new TestWebServiceImpl();
    private final QuestWebServiceImpl questWebService = new QuestWebServiceImpl();
    private final EpAuthenticationProvider epAuthenticationProvider = new EpAuthenticationProvider();

    private final String EXISTS_QUESTIONS = "existsQuestions";
    private final String QUESTIONS_LIST = "myQuestionnaire";
    private final String SAVE_TEST_ERROR = "saveTestError";
    private final String TEST_RESULT_MESSAGE_FORMAT = "Você acertou %.0f%% das questoes do teste";

    private boolean saveTestError;

    private final Questionnaire temporaryQuestionary = null;
    public String RESULT_MESSAGE = "resultadoMessage";

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
    public String getResultKnowledgeTestPage(final Questionnaire questionnaire, final Model model) {
        model.addAttribute(SAVE_TEST_ERROR, saveTestError);
        saveTestError = false;

        final String message = String.format(TEST_RESULT_MESSAGE_FORMAT, calculePercentage(questionnaire.getQuestions()));

        model.addAttribute(RESULT_MESSAGE, message);
        model.addAttribute(QUESTIONS_LIST, questionnaire);
        return FoldersName.KNOWLEDGE_TEST + "/result_test";
    }

    @PostMapping("/knowledge-test/save-result-test")
    public String getSaveResultKnowledgeTestPage(final Questionnaire questionnaire, final Model model) {
        final Usuario user = epAuthenticationProvider.getAuthenticatedUser();
        if (user == null) {
            saveTestError = true;
            return getResultKnowledgeTestPage(questionnaire, model);
        }

        questionnaire.getQuestions().stream().forEach(question -> question.setUserId(user.getId()));
        final long newTestId = testWebService.create(questionnaire.getQuestions());
        System.out.println(newTestId);
        if (newTestId == -1) {
            saveTestError = true;
            return getResultKnowledgeTestPage(questionnaire, model);
        }
        return "redirect:/knowledge-test/select-level";
    }

    @GetMapping("/knowledge-test/user-tests")
    public String getUserTestsPage(final Model model) {
        final Usuario authenticatedUser = epAuthenticationProvider.getAuthenticatedUser();
        if (authenticatedUser == null) {
            return "redirect:/not-found";
        }

        final Map<String, Long> criteria = new HashMap<>();
        criteria.put(Teste.TEST_TABLE.ID_USER_COLUMN, authenticatedUser.getId());
        final List<Teste> testList = (List<Teste>) testWebService.readByCriteria(criteria);

        final boolean exitsTests = (testList != null && !testList.isEmpty()) ? true : false;

        model.addAttribute("existsTests", exitsTests);
        model.addAttribute("testList", testList);
        return FoldersName.KNOWLEDGE_TEST + "/user_test_list";
    }

    @GetMapping("/knowledge-test/test-detail/{id}")
    public String getTestDetilPage(@PathVariable("id") final long id, final Model model) {
        final Questionnaire questionnaire = new Questionnaire();
        final List<QuestionDto> questionDtoList = testWebService.readAllQuestionsByTest(id);

        String message = "";
        boolean existsQuestions = false;
        if (questionDtoList != null && !questionDtoList.isEmpty()) {
            existsQuestions = true;
            questionDtoList.stream().forEach(questionnaire::addQuestion);
            message = String.format(TEST_RESULT_MESSAGE_FORMAT, calculePercentage(questionDtoList));
        }

        model.addAttribute(RESULT_MESSAGE, message);
        model.addAttribute(QUESTIONS_LIST, questionnaire);
        model.addAttribute(EXISTS_QUESTIONS, existsQuestions);
        return FoldersName.KNOWLEDGE_TEST + "/user_test_detail";
    }

    private double calculePercentage(final List<QuestionDto> questionDtoList) {
        final double correctAnswers = questionDtoList.stream().
                filter(question -> question.getUserAswer().equalsIgnoreCase(question.getResposta()))
                .collect(Collectors.toList()).size();
        return (correctAnswers / questionDtoList.size()) * 100;
    }
}