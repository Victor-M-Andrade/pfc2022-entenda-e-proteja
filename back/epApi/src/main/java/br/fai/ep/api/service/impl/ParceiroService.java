package br.fai.ep.api.service.impl;

import br.fai.ep.api.db.dao.impl.ParceiroDao;
import br.fai.ep.api.db.helper.DataBaseHelper.PartinerRequestTable;
import br.fai.ep.api.db.helper.DataBaseHelper.Sql;
import br.fai.ep.api.entities.BasePojo;
import br.fai.ep.api.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ParceiroService implements BaseService {
    @Autowired
    private ParceiroDao dao;

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
        String queryCriteria = Sql.WHERE.getName() + "1=1";
        for (final Object key : criteria.keySet()) {
            final PartinerRequestTable column = (PartinerRequestTable) key;
            queryCriteria += column.getName() + Sql.EQUAL_COMPATION.getName();
            queryCriteria += criteria.get(key);
        }
        return dao.readByCriteria(queryCriteria);
    }
}
