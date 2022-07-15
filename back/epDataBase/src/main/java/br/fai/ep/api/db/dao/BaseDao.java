package br.fai.ep.api.db.dao;

import br.fai.ep.api.entities.BasePojo;

import java.sql.SQLException;
import java.util.List;

public interface BaseDao<T> {

    public List<? extends BasePojo> readAll();

    public T readById(long id);

    public long create(T entity);

    public boolean update(T entity);

    public boolean delete(long id);

    public List<? extends BasePojo> readByCriteria(String criteria);

    public void preparForReadingOrCreating(String sql, boolean isGenerateKeys, boolean isAutoCommit) throws SQLException;

    public void preparForUpdateOrDelete(String sql) throws SQLException;

    public void resetValuesForNewQuery();
}
