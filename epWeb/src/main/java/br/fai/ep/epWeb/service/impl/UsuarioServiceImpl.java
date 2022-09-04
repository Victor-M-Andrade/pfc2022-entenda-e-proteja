package br.fai.ep.epWeb.service.impl;

import br.fai.ep.epEntities.BasePojo;
import br.fai.ep.epEntities.Usuario;
import br.fai.ep.epWeb.service.BaseServiceWeb;
import br.fai.ep.epWeb.service.ServiceInterface;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class UsuarioServiceImpl extends BaseServiceWeb implements ServiceInterface {
    private final String baseEntedpoint = API_HOST + "/user";

    @Override
    public List<? extends BasePojo> readAll() {
        final String endpoint = baseEntedpoint + "/read-all";
        List<Usuario> response = null;

        try {
            final RestTemplate restTemplate = new RestTemplate();
            final HttpEntity<String> httpEntity = new HttpEntity<>("");
            final ResponseEntity<Usuario[]> requestResponse = restTemplate.exchange(endpoint, HttpMethod.GET, httpEntity, Usuario[].class);
            response = Arrays.asList(requestResponse.getBody());
        } catch (final Exception ex) {
            System.out.println(ex.getMessage());
        }

        return response;
    }

    @Override
    public Object readById(final long id) {
        final String endpoint = baseEntedpoint + "/read-by-id/" + id;
        Usuario response = null;

        try {
            final RestTemplate restTemplate = new RestTemplate();
            final HttpEntity<String> httpEntity = new HttpEntity<>("");
            final ResponseEntity<Usuario> requestResponse = restTemplate.exchange(endpoint, HttpMethod.GET,
                    httpEntity, Usuario.class);
            response = requestResponse.getBody();
        } catch (final Exception ex) {
            System.out.println(ex.getMessage());
        }

        return response;
    }

    @Override
    public long create(final Object entity) {
        final String endpoint = baseEntedpoint + "/create";
        long newIdUser = Long.valueOf(-1);

        try {
            final RestTemplate restTemplace = new RestTemplate();
            final HttpEntity<Usuario> httpEntity = new HttpEntity<>((Usuario) entity);
            final ResponseEntity<Integer> responseEntity = restTemplace.exchange(endpoint, HttpMethod.POST,
                    httpEntity, Integer.class);
            newIdUser = responseEntity.getBody();
        } catch (final Exception e) {
            System.out.println(e.getMessage());
        }

        return newIdUser;
    }

    @Override
    public boolean update(final Object entity) {
        final String endpoint = baseEntedpoint + "/update";
        boolean response = false;

        try {
            final RestTemplate restTemplate = new RestTemplate();
            final HttpEntity<Usuario> httpEntity = new HttpEntity<>((Usuario) entity);
            final ResponseEntity<Boolean> responseEntity = restTemplate.exchange(endpoint, HttpMethod.PUT,
                    httpEntity, Boolean.class);
            response = responseEntity.getBody();
        } catch (final Exception ex) {
            System.out.println(ex.getMessage());
        }

        return response;
    }

    @Override
    public boolean delete(final long id) {
        final String endpoint = baseEntedpoint + "/delete/" + id;
        boolean response = false;

        try {
            final RestTemplate restTemplace = new RestTemplate();
            final HttpEntity<String> httpEntity = new HttpEntity<>("");
            final ResponseEntity<Boolean> requestResponse = restTemplace.exchange(endpoint, HttpMethod.DELETE,
                    httpEntity, Boolean.class);
            response = requestResponse.getBody();

        } catch (final Exception e) {
            System.out.println(e.getMessage());
        }

        return response;
    }

    @Override
    public List<? extends BasePojo> readByCriteria(final Map criteria) {
        final String endpoint = baseEntedpoint + "/read-by-criteria";
        List<Usuario> response = null;

        try {
            final RestTemplate restTemplace = new RestTemplate();
            final HttpEntity<Map> httpEntity = new HttpEntity<>(criteria);
            final ResponseEntity<Usuario[]> responseEntity = restTemplace.exchange(endpoint, HttpMethod.POST,
                    httpEntity, Usuario[].class);
            response = Arrays.asList(responseEntity.getBody());

        } catch (final Exception ex) {
            System.out.println(ex.getMessage());
        }

        return response;
    }
}