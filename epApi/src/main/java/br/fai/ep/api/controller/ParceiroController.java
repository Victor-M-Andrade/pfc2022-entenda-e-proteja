package br.fai.ep.api.controller;

import br.fai.ep.api.service.impl.ParceiroServiceImpl;
import br.fai.ep.epEntities.BasePojo;
import br.fai.ep.epEntities.Parceiro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/partner")
@CrossOrigin(origins = "*")
public class ParceiroController {

    @Autowired
    private ParceiroServiceImpl service;

    @GetMapping("/read-all")
    public ResponseEntity<List<? extends BasePojo>> readAll() {

        return ResponseEntity.ok(service.readAll());
    }

    @GetMapping("/read-by-id/{id}")
    public ResponseEntity readById(@PathVariable("id") final long id) {
        return ResponseEntity.ok(service.readById(id));
    }

    @GetMapping("/read-partner-detail/{id}")
    public ResponseEntity readPartnerDetail(@PathVariable("id") final long id) {
        return ResponseEntity.ok(service.readById(id));
    }


    @PostMapping("/create")
    public ResponseEntity<Long> create(@RequestBody final Parceiro entity) {
        System.out.println(entity);
        return ResponseEntity.ok(service.create(entity));
    }

    @PutMapping("/update")
    public ResponseEntity<Boolean> update(@RequestBody final Parceiro entity) {
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