package br.fai.ep.epWeb.helper;

import br.fai.ep.epEntities.DTO.QuestionDto;

import java.util.ArrayList;
import java.util.List;

public class Questionnaire {
    private final List<QuestionDto> questions = new ArrayList<>();

    public List<QuestionDto> getQuestions() {
        return questions;
    }

    public void addQuestion(final QuestionDto question) {
        questions.add(question);
    }

}