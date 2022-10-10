package br.fai.ep.api.controller;

import br.fai.ep.api.service.impl.NoticiaServiceImpl;
import br.fai.ep.epEntities.BasePojo;
import br.fai.ep.epEntities.DTO.NewsDto;
import br.fai.ep.epEntities.Noticia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/news")
@CrossOrigin(origins = "*")
public class NoticiaController {

    @Autowired
    private NoticiaServiceImpl service;

    @GetMapping("/read-all")
    public ResponseEntity<List<? extends BasePojo>> readAll() {
        return ResponseEntity.ok(service.readAll());
    }

    @GetMapping("/read-by-id/{id}")
    public ResponseEntity readById(@PathVariable("id") final long id) {
        return ResponseEntity.ok(service.readById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<Long> create(@RequestBody final Noticia entity) {
        System.out.println(entity);
        return ResponseEntity.ok(service.create(entity));
    }

    @PutMapping("/update")
    public ResponseEntity<Boolean> update(@RequestBody final Noticia entity) {
        return ResponseEntity.ok(service.update(entity));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable("id") final long id) {
        return ResponseEntity.ok(service.delete(id));
    }

    @PostMapping("/read-by-criteria")
    public ResponseEntity<List<? extends BasePojo>> readByCriteria(@RequestBody final Map criteria) {
        return ResponseEntity.ok(service.readByCriteria(criteria));
    }

    @GetMapping("/read-all-dto")
    public ResponseEntity<List<NewsDto>> readAllNewsDto() {
        return ResponseEntity.ok(service.readAllNewsDto());
    }

    @GetMapping("/read-by-dto-id/{id}")
    public ResponseEntity readByNewsDtoId(@PathVariable("id") final long id) {
        return ResponseEntity.ok(service.readByNewsDtoId(id));
    }

    @PostMapping("/read-by-dto-criteria")
    public ResponseEntity<List<NewsDto>> readByDtoCriteria(@RequestBody final Map criteria) {
        return ResponseEntity.ok(service.readByDtoCriteria(criteria));
    }

    @GetMapping("/read-all-limit/{limit}")
    public ResponseEntity<List<? extends BasePojo>> readLastNewsWithLimit(@PathVariable("limit") final int limit) {
        return ResponseEntity.ok(service.readLastNewsWithLimit(limit));
    }

    @GetMapping("/read-all-dto-limit/{limit}")
    public ResponseEntity<List<NewsDto>> readLastNewsDtoWithLimit(@PathVariable("limit") final int limit) {
        return ResponseEntity.ok(service.readLastNewsDtoWithLimit(limit));
    }

    @PostMapping("/last-news-by-dto-criteria-with-limit/{limit}")
    public ResponseEntity<List<NewsDto>> readLastNewsDtoWithLimit(@RequestBody final Map criteria, @PathVariable("limit") final int limit) {
        return ResponseEntity.ok(service.readLastNewsByDtoCriteriaWithLimit(criteria, limit));
    }

}