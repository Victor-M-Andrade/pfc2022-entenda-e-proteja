package br.fai.ep.api.controller;

import br.fai.ep.api.service.impl.QuestaoServiceImpl;
import br.fai.ep.epEntities.BasePojo;
import br.fai.ep.epEntities.DTO.QuestionDto;
import br.fai.ep.epEntities.Questao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/question")
@CrossOrigin(origins = "*")
public class QuestaoController {

    @Autowired
    private QuestaoServiceImpl service;

    @GetMapping("/read-all")
    public ResponseEntity<List<? extends BasePojo>> readAll() {
        return ResponseEntity.ok(service.readAll());
    }

    @GetMapping("/read-by-id/{id}")
    public ResponseEntity readById(@PathVariable("id") final long id) {
        return ResponseEntity.ok(service.readById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<Long> create(@RequestBody final Questao entity) {
        System.out.println(entity);
        return ResponseEntity.ok(service.create(entity));
    }

    @PutMapping("/update")
    public ResponseEntity<Boolean> update(@RequestBody final Questao entity) {
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

    @PostMapping("/read-by-dto-criteria")
    public ResponseEntity<List<QuestionDto>> readByDtoCritia(@RequestBody final Map criteria) {
        return ResponseEntity.ok(service.readByDtoCritia(criteria));
    }
}