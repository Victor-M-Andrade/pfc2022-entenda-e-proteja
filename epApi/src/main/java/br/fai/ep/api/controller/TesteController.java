package br.fai.ep.api.controller;

import br.fai.ep.api.service.impl.TesteServiceImpl;
import br.fai.ep.epEntities.BasePojo;
import br.fai.ep.epEntities.DTO.QuestionDto;
import br.fai.ep.epEntities.Teste;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "*")
public class TesteController {

    @Autowired
    private TesteServiceImpl service;

    @GetMapping("/read-all")
    public ResponseEntity<List<? extends BasePojo>> readAll() {
        return ResponseEntity.ok(service.readAll());
    }

    @GetMapping("/read-by-id/{id}")
    public ResponseEntity readById(@PathVariable("id") final long id) {
        return ResponseEntity.ok(service.readById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<Long> create(@RequestBody final List<QuestionDto> questionsList) {
        return ResponseEntity.ok(service.create(questionsList));
    }

    @PutMapping("/update")
    public ResponseEntity<Boolean> update(@RequestBody final Teste entity) {
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

    @GetMapping("/read-test-by-question/{id}")
    public ResponseEntity<List<Teste>> readAllTestsByQuestion(@PathVariable("id") final long id) {
        return ResponseEntity.ok(service.readAllTestsByQuestion(id));
    }

    @GetMapping("/read-question-by-test/{id}")
    public ResponseEntity<List<QuestionDto>> readAllQuestionsByTest(@PathVariable("id") final long id) {
        return ResponseEntity.ok(service.readAllQuestionsByTest(id));
    }
}