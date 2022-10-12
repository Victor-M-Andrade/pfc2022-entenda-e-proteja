package br.fai.ep.epWeb.service;

import br.fai.ep.epEntities.Usuario;
import br.fai.ep.epEntities.security.AesEncryptor;
import br.fai.ep.epEntities.security.HeaderPattern;
import br.fai.ep.epWeb.security.CustomUserDetail;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Base64;

public class RestService {

    public static HttpHeaders getAuthenticationHeaders(final String username, final String password) {
        final String auth = "Username=" + username + ";Password=" + password;
        try {
            final byte[] AesEncryptedMessage = AesEncryptor.encrypt(auth);
            final byte[] encondedBytes = Base64.getEncoder().encode(AesEncryptedMessage);
            final String header = String.format(HeaderPattern.HEADER_AES_FORMAT, new String(encondedBytes));
            final HttpHeaders headers = new HttpHeaders();
            headers.set(HeaderPattern.HEADER_AUTHORIZATION, header);
            return headers;

        } catch (final Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static HttpHeaders getRequestHeaders() {
        try {
            final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            final CustomUserDetail userDatails = (CustomUserDetail) authentication.getPrincipal();

            final Usuario authenticatedUser = userDatails.getUsuario();

            System.out.println("Cliente - Token do Usuario: " + authenticatedUser.getToken());
            final String authHeader = String.format(HeaderPattern.HEADER_PREFIX_FORMAT, authenticatedUser.getToken());

            final HttpHeaders headers = new HttpHeaders();
            headers.set(HeaderPattern.HEADER_AUTHORIZATION, authHeader);

            return headers;
        } catch (final Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}