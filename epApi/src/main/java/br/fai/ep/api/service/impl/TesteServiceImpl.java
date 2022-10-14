package br.fai.ep.api.service.impl;

import br.fai.ep.api.service.KnowledgeTestService;
import br.fai.ep.db.dao.impl.QuestaoTesteDaoImpl;
import br.fai.ep.db.dao.impl.TesteDaoImpl;
import br.fai.ep.db.helper.DataBaseHelper.SQL_COMMAND;
import br.fai.ep.epEntities.BasePojo;
import br.fai.ep.epEntities.DTO.QuestionDto;
import br.fai.ep.epEntities.Teste;
import br.fai.ep.epEntities.Teste.TEST_TABLE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TesteServiceImpl implements KnowledgeTestService {
    @Autowired
    private TesteDaoImpl dao;

    private final QuestaoTesteDaoImpl questTestDao = new QuestaoTesteDaoImpl();

    @Override
    public List<Teste> readAll() {
        return (List<Teste>) dao.readAll();
    }

    @Override
    public Teste readById(final long id) {
        return (Teste) dao.readById(id);
    }

    @Override
    public long create(final List<QuestionDto> questionDtoList) {

        final int cont = questionDtoList.stream().filter(questionDto -> questionDto.getUserAswer().equalsIgnoreCase(questionDto.getResposta()))
                .collect(Collectors.toList()).size();
        final Teste test = new Teste();
        test.setAcertos(cont);
        test.setIdUsuario(questionDtoList.get(0).getUserId());
        final long newIdTest = dao.create(test);
        if (newIdTest == -1) {
            return newIdTest;
        }
        questionDtoList.stream().forEach(questionDto -> questionDto.setTestId(newIdTest));
        if (questTestDao.create(questionDtoList) == -1) {
            delete(newIdTest);
            return -1;
        }
        return newIdTest;
    }

    @Override
    public boolean update(final Teste entity) {
        return dao.update(entity);
    }

    @Override
    public boolean delete(final long id) {
        return dao.delete(id);
    }

    @Override
    public List<? extends BasePojo> readByCriteria(final Map criteria) {
        String queryCriteria = SQL_COMMAND.WHERE + " 1=1 ";
        queryCriteria += buildCriteriaParameters(criteria) + ";";
        return dao.readByCriteria(queryCriteria);
    }

    @Override
    public String buildCriteriaParameters(final Map criteria) {
        String param = "";
        for (final Object key : criteria.keySet()) {
            final String column = (String) key;
            if (TEST_TABLE.DATE_TIME_COLUMN.equalsIgnoreCase(column)) {
                param += SQL_COMMAND.AND + column + "::text" + SQL_COMMAND.ILIKE + "\'%" + criteria.get(key) + "%\'";
                continue;
            }
            param += SQL_COMMAND.AND + column + SQL_COMMAND.EQUAL_COMPATION + criteria.get(key);
        }

        return param;
    }
}