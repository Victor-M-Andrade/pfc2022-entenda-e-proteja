package br.fai.ep.api.db.dao;

import br.fai.ep.api.entities.BasePojo;

import java.util.List;

public interface BaseDaoInterface<T> {

    List<? extends BasePojo> readAll();

    T readById(long id);

    long create(T entity);

    boolean update(T entity);

    boolean delete(long id);

    List<? extends BasePojo> readByCriteria(String criteria);
}