package br.fai.ep.db.dao;

import br.fai.ep.epEntities.BasePojo;

import java.util.List;

public interface BaseDaoInterface<T> {

    List<? extends BasePojo> readAll();

    T readById(long id);

    long create(T entity);

    boolean update(T entity);

    boolean delete(long id);

    List<? extends BasePojo> readByCriteria(String criteria);
}