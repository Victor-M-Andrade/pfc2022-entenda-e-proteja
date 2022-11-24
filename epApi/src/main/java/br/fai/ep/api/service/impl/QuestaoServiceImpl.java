package br.fai.ep.api.service.impl;

import br.fai.ep.api.service.BaseService;
import br.fai.ep.db.dao.impl.QuestaoDaoImpl;
import br.fai.ep.db.dao.impl.QuestaoTesteDaoImpl;
import br.fai.ep.db.dao.impl.TesteDaoImpl;
import br.fai.ep.db.helper.DataBaseHelper.SQL_COMMAND;
import br.fai.ep.epEntities.BasePojo;
import br.fai.ep.epEntities.DTO.QuestionDto;
import br.fai.ep.epEntities.Questao.QUESTION_TABLE;
import br.fai.ep.epEntities.Teste;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class QuestaoServiceImpl implements BaseService {
    @Autowired
    private QuestaoDaoImpl dao;

    private final TesteDaoImpl testDao = new TesteDaoImpl();
    private final QuestaoTesteDaoImpl questTestDao = new QuestaoTesteDaoImpl();

    @Override
    public List<? extends BasePojo> readAll() {
        return dao.readAll();
    }

    @Override
    public Object readById(final long id) {
        return dao.readById(id);
    }

    @Override
    public long create(final Object entity) {
        return dao.create(entity);
    }

    @Override
    public boolean update(final Object entity) {
        return dao.update(entity);
    }

    @Override
    public boolean delete(final long id) {
        final List<Teste> testList = questTestDao.readAllTestsByQuestion(id);
        final List<Teste> updatedTestList = new ArrayList<>();
        boolean updated = false;
        for (final Teste test : testList) {
            if (test.getAcertos() > 0) {
                test.setAcertos(test.getAcertos() - 1);
            }
            updated = !testDao.update(test);
            if (updated) {
                break;
            }
            updatedTestList.add(test);
        }

        if (updated) {
            for (final Teste test : updatedTestList) {
                test.setAcertos(test.getAcertos() + 1);
                testDao.update(test);
            }
            return false;
        }

        if (!dao.delete(id)) {
            for (final Teste test : updatedTestList) {
                test.setAcertos(test.getAcertos());
                testDao.update(test);
            }
            return false;
        }

        return true;
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
            if (QUESTION_TABLE.LEVEL_COLUMN.equalsIgnoreCase(column)) {
                param += SQL_COMMAND.AND + column + SQL_COMMAND.EQUAL_COMPATION + criteria.get(key);
                continue;
            }
            param += SQL_COMMAND.AND + column + SQL_COMMAND.ILIKE + "\'%" + criteria.get(key) + "%\'";
        }

        return param;
    }

    public List<QuestionDto> readByDtoCritia(final Map criteria) {
        String queryCriteria = SQL_COMMAND.WHERE + " 1=1 ";
        queryCriteria += buildCriteriaParameters(criteria) + ";";
        return dao.readByDtoCritia(queryCriteria);
    }
}