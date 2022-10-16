package br.fai.ep.epWeb.service.impl;

import br.fai.ep.epEntities.BasePojo;
import br.fai.ep.epEntities.DTO.QuestionDto;
import br.fai.ep.epEntities.DTO.TestDto;
import br.fai.ep.epEntities.Teste;
import br.fai.ep.epWeb.service.BaseWebService;
import br.fai.ep.epWeb.service.RestService;
import br.fai.ep.epWeb.service.WebServiceInterface;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class TestWebServiceImpl extends BaseWebService implements WebServiceInterface {
    private final String BASE_ENDPOINT = API_HOST + "/test";

    @Override
    public List<? extends BasePojo> readAll() {
        final String endpoint = BASE_ENDPOINT + "/read-all";
        List<Teste> response = null;

        try {
            final RestTemplate restTemplate = new RestTemplate();
            final HttpEntity<String> httpEntity = new HttpEntity<>(RestService.getRequestHeaders());
            final ResponseEntity<Teste[]> requestResponse = restTemplate.exchange(endpoint, HttpMethod.GET, httpEntity, Teste[].class);
            response = Arrays.asList(requestResponse.getBody());
        } catch (final Exception ex) {
            System.out.println(ex.getMessage());
        }

        return response;
    }

    @Override
    public Object readById(final long id) {
        final String endpoint = BASE_ENDPOINT + "/read-by-id/" + id;
        Teste response = null;

        try {
            final RestTemplate restTemplate = new RestTemplate();
            final HttpEntity<String> httpEntity = new HttpEntity<>(RestService.getRequestHeaders());
            final ResponseEntity<Teste> requestResponse = restTemplate.exchange(endpoint, HttpMethod.GET,
                    httpEntity, Teste.class);
            response = requestResponse.getBody();
        } catch (final Exception ex) {
            System.out.println(ex.getMessage());
        }

        return response;
    }

    @Override
    public long create(final Object entity) {
        final String endpoint = BASE_ENDPOINT + "/create";
        long newIdTeste = Long.valueOf(-1);

        try {
            final RestTemplate restTemplace = new RestTemplate();
            final HttpEntity<List<QuestionDto>> httpEntity = new HttpEntity<>((List<QuestionDto>) entity, RestService.getRequestHeaders());
            final ResponseEntity<Integer> responseEntity = restTemplace.exchange(endpoint, HttpMethod.POST,
                    httpEntity, Integer.class);
            newIdTeste = responseEntity.getBody();
        } catch (final Exception e) {
            System.out.println(e.getMessage());
        }

        return newIdTeste;
    }

    @Override
    public boolean update(final Object entity) {
        final String endpoint = BASE_ENDPOINT + "/update";
        boolean response = false;

        try {
            final RestTemplate restTemplate = new RestTemplate();
            final HttpEntity<Teste> httpEntity = new HttpEntity<>((Teste) entity, RestService.getRequestHeaders());
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
            final HttpEntity<String> httpEntity = new HttpEntity<>(RestService.getRequestHeaders());
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
        List<Teste> response = null;

        try {
            final RestTemplate restTemplace = new RestTemplate();
            final HttpEntity<Map> httpEntity = new HttpEntity<>(criteria, RestService.getRequestHeaders());
            final ResponseEntity<Teste[]> responseEntity = restTemplace.exchange(endpoint, HttpMethod.POST,
                    httpEntity, Teste[].class);
            response = Arrays.asList(responseEntity.getBody());

        } catch (final Exception ex) {
            System.out.println(ex.getMessage());
        }

        return response;
    }

    @Override
    public String buildNameNewFile(final Object entity) {
        return "";
    }

    public List<Teste> readAllTestsByQuestion(final long testId) {
        final String endpoint = BASE_ENDPOINT + "/read-test-by-question/" + testId;
        List<Teste> response = null;

        try {
            final RestTemplate restTemplate = new RestTemplate();
            final HttpEntity<String> httpEntity = new HttpEntity<>(RestService.getRequestHeaders());
            final ResponseEntity<Teste[]> requestResponse = restTemplate.exchange(endpoint, HttpMethod.GET,
                    httpEntity, Teste[].class);
            response = Arrays.asList(requestResponse.getBody());
        } catch (final Exception ex) {
            System.out.println(ex.getMessage());
        }

        return response;
    }

    public List<QuestionDto> readAllQuestionsByTest(final long testId) {
        final String endpoint = BASE_ENDPOINT + "/read-question-by-test/" + testId;
        List<QuestionDto> response = null;

        try {
            final RestTemplate restTemplate = new RestTemplate();
            final HttpEntity<String> httpEntity = new HttpEntity<>(RestService.getRequestHeaders());
            final ResponseEntity<QuestionDto[]> requestResponse = restTemplate.exchange(endpoint, HttpMethod.GET,
                    httpEntity, QuestionDto[].class);
            response = Arrays.asList(requestResponse.getBody());
        } catch (final Exception ex) {
            System.out.println(ex.getMessage());
        }

        return response;
    }

    public List<TestDto> readAllUserTest(final long userId) {
        final String endpoint = BASE_ENDPOINT + "/read-all-user-test/" + userId;
        List<TestDto> response = null;

        try {
            final RestTemplate restTemplate = new RestTemplate();
            final HttpEntity<String> httpEntity = new HttpEntity<>(RestService.getRequestHeaders());
            final ResponseEntity<TestDto[]> requestResponse = restTemplate.exchange(endpoint, HttpMethod.GET,
                    httpEntity, TestDto[].class);
            response = Arrays.asList(requestResponse.getBody());
        } catch (final Exception ex) {
            System.out.println(ex.getMessage());
        }

        return response;
    }
}