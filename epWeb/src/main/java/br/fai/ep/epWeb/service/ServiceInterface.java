package br.fai.ep.epWeb.service;

import br.fai.ep.epEntities.BasePojo;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public interface ServiceInterface<T> {

    List<? extends BasePojo> readAll();

    T readById(long id);

    long create(T entity);

    boolean update(T entity);

    boolean delete(long id);

    List<? extends BasePojo> readByCriteria(Map<String, String> criteria);

    default String getCreationDate(final Timestamp timestamp) {
        return BaseServiceWeb.dateFormate(timestamp);
    }

    default String getCreationDateAndTime(final Timestamp timestamp) {
        return BaseServiceWeb.dateFormateComplete(timestamp);
    }
}