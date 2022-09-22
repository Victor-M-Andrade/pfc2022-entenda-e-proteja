package br.fai.ep.api.service.impl;

import br.fai.ep.api.service.BaseService;
import br.fai.ep.db.dao.impl.ClienteDaoImpl;
import br.fai.ep.db.helper.DataBaseHelper.SQL_COMMAND;
import br.fai.ep.epEntities.BasePojo;
import br.fai.ep.epEntities.Cliente.CLIENT_TABLE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ClienteServiceImpl implements BaseService {
    @Autowired
    private ClienteDaoImpl dao;

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
            if (CLIENT_TABLE.DATE_TIME_COLUMN.equalsIgnoreCase(column)) {
                param += SQL_COMMAND.AND + column + "::text" + SQL_COMMAND.ILIKE + "\'%" + criteria.get(key) + "%\'";
                continue;
            } else if (CLIENT_TABLE.ID_COLUMN.equalsIgnoreCase(column)) {
                param += SQL_COMMAND.AND + column + SQL_COMMAND.EQUAL_COMPATION + criteria.get(key);
                continue;
            }
            param += SQL_COMMAND.AND + column + SQL_COMMAND.ILIKE + "\'%" + criteria.get(key) + "%\'";
        }
        return param;
    }
}