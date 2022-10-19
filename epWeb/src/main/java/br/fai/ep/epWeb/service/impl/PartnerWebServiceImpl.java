package br.fai.ep.epWeb.service.impl;

import br.fai.ep.epEntities.BasePojo;
import br.fai.ep.epEntities.Parceiro;
import br.fai.ep.epWeb.helper.AnonymizeData;
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

public class PartnerWebServiceImpl extends BaseWebService implements WebServiceInterface {
    private final String BASE_ENDPOINT = API_HOST + "/partner";
    private final String NAME_FILE_FORMAT = "PARTNER_IMAGE_ID__%d_%s";

    @Override
    public List<? extends BasePojo> readAll() {
        final String endpoint = BASE_ENDPOINT + "/read-all";
        List<Parceiro> response = null;

        try {
            final RestTemplate restTemplate = new RestTemplate();
            final HttpEntity<String> httpEntity = new HttpEntity<>(RestService.getRequestHeaders());
            final ResponseEntity<Parceiro[]> requestResponse = restTemplate.exchange(endpoint, HttpMethod.GET, httpEntity, Parceiro[].class);
            response = Arrays.asList(requestResponse.getBody());
        } catch (final Exception ex) {
            System.out.println(ex.getMessage());
        }

        if (response != null && !response.isEmpty()) {
            response.stream().forEach(AnonymizeData::anonymizeAllData);
        }

        return response;
    }

    @Override
    public Object readById(final long id) {
        final String endpoint = BASE_ENDPOINT + "/read-by-id/" + id;
        Parceiro response = null;

        try {
            final RestTemplate restTemplate = new RestTemplate();
            final HttpEntity<String> httpEntity = new HttpEntity<>(RestService.getRequestHeaders());
            final ResponseEntity<Parceiro> requestResponse = restTemplate.exchange(endpoint, HttpMethod.GET,
                    httpEntity, Parceiro.class);
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
            final HttpEntity<Parceiro> httpEntity = new HttpEntity<>((Parceiro) entity, RestService.getRequestHeaders());
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
            final HttpEntity<Parceiro> httpEntity = new HttpEntity<>((Parceiro) entity, RestService.getRequestHeaders());
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
        List<Parceiro> response = null;

        try {
            final RestTemplate restTemplace = new RestTemplate();
            final HttpEntity<Map> httpEntity = new HttpEntity<>(criteria, RestService.getRequestHeaders());
            final ResponseEntity<Parceiro[]> responseEntity = restTemplace.exchange(endpoint, HttpMethod.POST,
                    httpEntity, Parceiro[].class);
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
        final Parceiro partner = (Parceiro) entity;
        return String.format(NAME_FILE_FORMAT, partner.getId(), dateFormateForSaveFiles(partner.getDataHora()));
    }

    public Object readPartnerDetail(final long id) {
        final String endpoint = BASE_ENDPOINT + "/read-partner-detail/" + id;
        Parceiro response = null;

        try {
            final RestTemplate restTemplate = new RestTemplate();
            final HttpEntity<String> httpEntity = new HttpEntity<>(RestService.getRequestHeaders());
            final ResponseEntity<Parceiro> requestResponse = restTemplate.exchange(endpoint, HttpMethod.GET,
                    httpEntity, Parceiro.class);
            response = requestResponse.getBody();
        } catch (final Exception ex) {
            System.out.println(ex.getMessage());
        }

        if (response != null) {
            response = response.isAnonimo() ? AnonymizeData.anonymizeAllData(response) : response;
        }

        return response;
    }
}