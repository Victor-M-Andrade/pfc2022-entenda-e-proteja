package br.fai.ep.api.controller.impl;

import br.fai.ep.api.controller.BaseController;
import br.fai.ep.api.entities.BasePojo;
import br.fai.ep.api.service.impl.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class UsuarioController implements BaseController {

    @Autowired
    private UsuarioService service;

    @Override
    @GetMapping("/read-all")
    public ResponseEntity<List<? extends BasePojo>> readAll() {

        return ResponseEntity.ok(service.readAll());
    }

    @Override
    @GetMapping("/read-by-id/{id}")
    public ResponseEntity readById(@PathVariable("id") final long id) {

        return ResponseEntity.ok(service.readById(id));
    }

    @Override
    @PostMapping("/create")
    public ResponseEntity<Long> create(@RequestBody final Object entity) {
        return ResponseEntity.ok(service.create(entity));
    }

    @Override
    @PutMapping("/update")
    public ResponseEntity<Boolean> update(final Object entity) {
        return ResponseEntity.ok(service.update(entity));
    }

    @Override
    @GetMapping("/delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable("id") final long id) {
        return ResponseEntity.ok(service.delete(id));
    }

    @Override
    @PostMapping
    public ResponseEntity<List<? extends BasePojo>> readByCriteria(@RequestBody final Map criteria) {
        return ResponseEntity.ok(service.readByCriteria(criteria));
    }
}

