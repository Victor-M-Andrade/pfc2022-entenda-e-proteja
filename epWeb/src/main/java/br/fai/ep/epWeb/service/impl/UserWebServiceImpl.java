package br.fai.ep.epWeb.service.impl;

import br.fai.ep.epEntities.BasePojo;
import br.fai.ep.epEntities.DTO.MailDto;
import br.fai.ep.epEntities.Usuario;
import br.fai.ep.epWeb.helper.AnonymizeData;
import br.fai.ep.epWeb.service.BaseWebService;
import br.fai.ep.epWeb.service.WebServiceInterface;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserWebServiceImpl extends BaseWebService implements WebServiceInterface {
    private final String BASE_ENDPOINT = API_HOST + "/user";
    private final String NAME_FILE_FORMAT = "USER_IMAGE_ID__%d_%s";

    @Override
    public List<? extends BasePojo> readAll() {
        final String endpoint = BASE_ENDPOINT + "/read-all";
        List<Usuario> response = null;

        try {
            final RestTemplate restTemplate = new RestTemplate();
            final HttpEntity<String> httpEntity = new HttpEntity<>("");
            final ResponseEntity<Usuario[]> requestResponse = restTemplate.exchange(endpoint, HttpMethod.GET, httpEntity, Usuario[].class);
            response = Arrays.asList(requestResponse.getBody());
        } catch (final Exception ex) {
            System.out.println(ex.getMessage());
        }

        if (response != null && !response.isEmpty()) {
            // realizando a anonimizacao dos dados do usuario
            response.stream().forEach(AnonymizeData::anonymizeAllData);
        }

        return response;
    }

    @Override
    public Object readById(final long id) {
        final String endpoint = BASE_ENDPOINT + "/read-by-id/" + id;
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

        if (response != null) {
            response = response.isAnonimo() ? AnonymizeData.anonymizeAllData(response) : response;
        }

        return response;
    }

    @Override
    public long create(final Object entity) {
        final String endpoint = BASE_ENDPOINT + "/create";
        long newIdUser = Long.valueOf(-1);

        try {
            final RestTemplate restTemplace = new RestTemplate();
            final Usuario user = (Usuario) entity;
            final HttpEntity<Usuario> httpEntity = new HttpEntity<>(user);
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
        final String endpoint = BASE_ENDPOINT + "/update";
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
        final String endpoint = BASE_ENDPOINT + "/delete/" + id;
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
        final String endpoint = BASE_ENDPOINT + "/read-by-criteria";
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

        if (response != null && !response.isEmpty()) {
            response.stream().forEach(AnonymizeData::anonymizeAllData);
        }

        return response;
    }

    @Override
    public String buildNameNewFile(final Object entity) {
        final Usuario user = (Usuario) entity;
        return String.format(NAME_FILE_FORMAT, user.getId(), dateFormateForSaveFiles(user.getDataHora()));
    }

    public boolean forgotPassword(final String userEmail) {
        final String endpoint = BASE_ENDPOINT + "/forgot-password";
        boolean response = false;

        try {
            final RestTemplate restTemplace = new RestTemplate();
            final HttpEntity<String> httpEntity = new HttpEntity<>(userEmail);
            final ResponseEntity<Boolean> responseEntity = restTemplace.exchange(endpoint, HttpMethod.POST,
                    httpEntity, Boolean.class);
            response = responseEntity.getBody();
        } catch (final Exception ex) {
            System.out.println(ex.getMessage());
        }

        return response;
    }

    public Usuario authentication(final String email, final String password) {
        final String endpoint = BASE_ENDPOINT + "/authentication";
        Usuario response = null;
        final Map<String, String> criteria = new HashMap<>();
        criteria.put(Usuario.USER_TABLE.EMAIL_COLUMN, email);
        criteria.put(Usuario.USER_TABLE.PASSWORD_COLUMN, password);

        try {
            final RestTemplate restTemplace = new RestTemplate();
            final HttpEntity<Map> httpEntity = new HttpEntity<>(criteria);
            final ResponseEntity<Usuario> responseEntity = restTemplace.exchange(endpoint, HttpMethod.POST,
                    httpEntity, Usuario.class);
            response = responseEntity.getBody();
        } catch (final Exception ex) {
            System.out.println(ex.getMessage());
        }

        return response;
    }

    public Boolean anonymizeUser(final long id) {
        final String endpoint = BASE_ENDPOINT + "/anonymize-user/" + id;
        boolean response = false;

        try {
            final RestTemplate restTemplace = new RestTemplate();
            final HttpEntity<String> httpEntity = new HttpEntity<>("");
            final ResponseEntity<Boolean> requestResponse = restTemplace.exchange(endpoint, HttpMethod.GET,
                    httpEntity, Boolean.class);
            response = requestResponse.getBody();
        } catch (final Exception e) {
            System.out.println(e.getMessage());
        }

        return response;
    }

    public Boolean removeUserAnonymization(final long id) {
        final String endpoint = BASE_ENDPOINT + "/remove-user-anonymization/" + id;
        boolean response = false;

        try {
            final RestTemplate restTemplace = new RestTemplate();
            final HttpEntity<String> httpEntity = new HttpEntity<>("");
            final ResponseEntity<Boolean> requestResponse = restTemplace.exchange(endpoint, HttpMethod.GET,
                    httpEntity, Boolean.class);
            response = requestResponse.getBody();
        } catch (final Exception e) {
            System.out.println(e.getMessage());
        }

        return response;
    }

    public Boolean sendEmail(final MailDto mailDto) {
        final String endpoint = BASE_ENDPOINT + "/send-mail";
        boolean response = false;

        try {
            final RestTemplate restTemplace = new RestTemplate();
            final HttpEntity<MailDto> httpEntity = new HttpEntity<>(mailDto);
            final ResponseEntity<Boolean> requestResponse = restTemplace.exchange(endpoint, HttpMethod.POST,
                    httpEntity, Boolean.class);
            response = requestResponse.getBody();
        } catch (final Exception e) {
            System.out.println(e.getMessage());
        }

        return response;
    }
}