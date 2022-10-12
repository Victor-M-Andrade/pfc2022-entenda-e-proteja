package br.fai.ep.epEntities.security;

public abstract class HeaderPattern {
    public static final String HEADER_AES_FORMAT = "AES %s";
    public static final String HEADER_PREFIX_FORMAT = "Bearer %s";

    public static final String HEADER_AES_PREFIX = "AES ";
    public static final String HEADER_BEARER_PREFIX = "Bearer ";
    public static final String HEADER_AUTHORIZATION = "Authorization";
}