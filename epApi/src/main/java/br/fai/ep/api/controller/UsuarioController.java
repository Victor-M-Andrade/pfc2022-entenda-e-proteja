package br.fai.ep.api.controller;

import br.fai.ep.api.service.impl.UsuarioServiceImpl;
import br.fai.ep.epEntities.BasePojo;
import br.fai.ep.epEntities.DTO.MailDto;
import br.fai.ep.epEntities.Usuario;
import br.fai.ep.epEntities.security.HeaderPattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    private UsuarioServiceImpl service;

    @GetMapping("/read-all")
    public ResponseEntity<List<? extends BasePojo>> readAll() {

        return ResponseEntity.ok(service.readAll());
    }

    @GetMapping("/read-by-id/{id}")
    public ResponseEntity readById(@PathVariable("id") final long id) {

        return ResponseEntity.ok(service.readById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<Long> create(@RequestBody final Usuario entity) {
        System.out.println(entity);
        return ResponseEntity.ok(service.create(entity));
    }

    @PutMapping("/update")
    public ResponseEntity<Boolean> update(@RequestBody final Usuario entity) {
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

    @PostMapping("/forgot-password")
    public ResponseEntity<Boolean> forgotPassword(@RequestBody final String userEmail) {
        return ResponseEntity.ok(service.forgotPassword(userEmail));
    }

    @PostMapping("/authentication")
    public ResponseEntity<Usuario> authenticate(@RequestHeader(HeaderPattern.HEADER_AUTHORIZATION) final String encodedData) {
        final Usuario user = service.authenticate(encodedData);
        if (user == null) {
            return ResponseEntity.badRequest().build();

        }
        return ResponseEntity.ok(user);
    }

    @GetMapping("/anonymize-user/{id}")
    public ResponseEntity<Boolean> anonymizeUser(@PathVariable("id") final long id) {
        return ResponseEntity.ok(service.anonymizeUser(id));
    }

    @GetMapping("/remove-user-anonymization/{id}")
    public ResponseEntity<Boolean> removeUserAnonymization(@PathVariable("id") final long id) {
        return ResponseEntity.ok(service.removeUserAnonymization(id));
    }

    @PostMapping("/send-mail")
    public ResponseEntity<Boolean> authenticate(@RequestBody final MailDto mail) {
        return ResponseEntity.ok(service.sendemail(mail));
    }
}