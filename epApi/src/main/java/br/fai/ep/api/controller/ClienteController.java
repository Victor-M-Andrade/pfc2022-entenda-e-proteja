package br.fai.ep.api.controller;

import br.fai.ep.epEntities.BasePojo;
import br.fai.ep.epEntities.Cliente;
import br.fai.ep.api.service.impl.ClienteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/client")
@CrossOrigin(origins = "*")
public class ClienteController {

    @Autowired
    private ClienteServiceImpl service;

    @GetMapping("/read-all")
    public ResponseEntity<List<? extends BasePojo>> readAll() {
        return ResponseEntity.ok(service.readAll());
    }

    @GetMapping("/read-by-id/{id}")
    public ResponseEntity readById(@PathVariable("id") final long id) {
        return ResponseEntity.ok(service.readById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<Long> create(@RequestBody final Cliente entity) {
        System.out.println(entity);
        return ResponseEntity.ok(service.create(entity));
    }

    @PutMapping("/update")
    public ResponseEntity<Boolean> update(@RequestBody final Cliente entity) {
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
}