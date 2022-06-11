package br.fai.ep.api.controller;

import br.fai.ep.api.entities.BasePojo;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface BaseController<T> {
    public ResponseEntity<List<? extends BasePojo>> readAll();

    public ResponseEntity<T> readById(long id);

    public ResponseEntity<Long> create(T entity);

    public ResponseEntity<Boolean> update(T entity);

    public ResponseEntity<Boolean> delete(long id);

    public ResponseEntity<List<? extends BasePojo>> readByCriteria(Map<? extends Enum, String> criteria);
}
