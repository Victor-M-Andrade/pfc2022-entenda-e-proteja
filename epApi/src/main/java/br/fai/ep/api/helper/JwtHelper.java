package br.fai.ep.api.helper;

import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

public abstract class JwtHelper {
    public static final String ID_NAME_TOKE = "PROJECT_EP_FAI";
    public static final String CLAIMS_AUTHORITIES_KEY = "authorities";
    public final static String INVALID_TOKEN = "INVALID_TOKEN";

    public static SecretKey KEY_GENERATE_TOKEN = Keys.hmacShaKeyFor("7f-j&Ckk=coNzZc0y7_4oMP?#TfcYq%fcD0mDpenW2nc!1fGoZ|d?f&RNbDHUX6".getBytes(StandardCharsets.UTF_8));
}