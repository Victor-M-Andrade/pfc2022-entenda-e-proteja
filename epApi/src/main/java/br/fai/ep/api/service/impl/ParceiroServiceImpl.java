package br.fai.ep.api.service.impl;

import br.fai.ep.api.db.dao.impl.ParceiroDaoImpl;
import br.fai.ep.api.db.helper.DataBaseHelper.SQL_COMMAND;
import br.fai.ep.api.entities.BasePojo;
import br.fai.ep.api.entities.Parceiro.PARTINER_TABLE;
import br.fai.ep.api.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ParceiroServiceImpl implements BaseService {
    @Autowired
    private ParceiroDaoImpl dao;

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
            if (PARTINER_TABLE.ID_USER_COLUMN.equalsIgnoreCase(column)) {
                param += SQL_COMMAND.OR + column + SQL_COMMAND.EQUAL_COMPATION + criteria.get(key);
                continue;
            } else if (PARTINER_TABLE.SITUATION_COLUMN.equalsIgnoreCase(column) ||
                    PARTINER_TABLE.SERVICE_TYPE_COLUMN.equalsIgnoreCase(column)) {
                param += SQL_COMMAND.OR + column + SQL_COMMAND.EQUAL_COMPATION + "'" + criteria.get(key) + "'";
                continue;
            }
            param += SQL_COMMAND.OR + column + SQL_COMMAND.ILIKE + "\'%" + criteria.get(key) + "%\'";
        }

        return param;
    }
}