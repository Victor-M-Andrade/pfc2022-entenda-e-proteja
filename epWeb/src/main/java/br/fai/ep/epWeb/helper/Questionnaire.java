package br.fai.ep.epWeb.helper;

import br.fai.ep.epEntities.DTO.QuestionDto;

import java.util.ArrayList;
import java.util.List;

public class Questionnaire {
    private List<QuestionDto> questions = new ArrayList<>();

    public List<QuestionDto> getQuestions() {
        return questions;
    }

    public void setQuestions(final List<QuestionDto> questions) {
        this.questions = questions;
    }

    public void addQuestion(final QuestionDto questao) {
        questions.add(questao);
    }

}