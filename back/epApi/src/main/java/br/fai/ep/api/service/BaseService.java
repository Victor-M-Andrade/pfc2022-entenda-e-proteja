package br.fai.ep.api.service;

import br.fai.ep.api.entities.BasePojo;

import java.util.List;
import java.util.Map;

public interface BaseService<T> {

    public List<? extends BasePojo> readAll();

    public T readById(long id);

    public long create(T entity);

    public boolean update(T entity);

    public boolean delete(long id);

    public List<? extends BasePojo> readByCriteria(Map<? extends Enum, String> criteria);

}
