package br.fai.ep.api.service;

import br.fai.ep.epEntities.BasePojo;

import java.util.List;
import java.util.Map;

public interface BaseService<T> {

    List<? extends BasePojo> readAll();

    T readById(long id);

    long create(T entity);

    boolean update(T entity);

    boolean delete(long id);

    List<? extends BasePojo> readByCriteria(Map<String, String> criteria);

    String buildCriteriaParameters(Map<String, String> criteria);
}