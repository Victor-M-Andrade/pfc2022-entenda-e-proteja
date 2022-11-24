package br.fai.ep.db.dao;

import br.fai.ep.epEntities.DTO.QuestionDto;
import br.fai.ep.epEntities.QuestaoTeste;
import br.fai.ep.epEntities.Teste;

import java.util.List;

public interface QuestTestDaoInterface {

    List<QuestaoTeste> readAll();

    QuestaoTeste readById(long id);

    long create(List<QuestionDto> entity);

    boolean update(QuestaoTeste entity);

    boolean delete(long id);

    List<QuestaoTeste> readByCriteria(String criteria);

    List<Teste> readAllTestsByQuestion(final long questionId);

    List<QuestionDto> readAllQuestionsByTest(final long testId);
}