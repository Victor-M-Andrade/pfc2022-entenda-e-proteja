package br.fai.ep.api.service;

import br.fai.ep.epEntities.Usuario;

public interface JwtService {

    String getJWTToken(Usuario user);

}