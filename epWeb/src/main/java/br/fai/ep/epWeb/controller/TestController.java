package br.fai.ep.epWeb.controller;

import br.fai.ep.epEntities.DTO.QuestionDto;
import br.fai.ep.epWeb.helper.FoldersName;
import br.fai.ep.epWeb.helper.Questionnaire;
import br.fai.ep.epWeb.service.impl.QuestWebServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class TestController {
    private final QuestWebServiceImpl questWebService = new QuestWebServiceImpl();

    public final String EXISTS_QUESTIONS = "existsQuestions";
    public final String QUESTIONS_LIST = "myQuestionnaire";

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
}