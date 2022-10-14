package br.fai.ep.api.service;

import br.fai.ep.epEntities.DTO.QuestionDto;
import br.fai.ep.epEntities.Teste;

import java.util.List;
import java.util.Map;

public interface KnowledgeTestService {

    List<Teste> readAll();

    Teste readById(long id);

    long create(List<QuestionDto> questionDtoList);

    boolean update(Teste entity);

    boolean delete(long id);

    List<Teste> readByCriteria(Map<String, String> criteria);

    String buildCriteriaParameters(Map<String, String> criteria);
}