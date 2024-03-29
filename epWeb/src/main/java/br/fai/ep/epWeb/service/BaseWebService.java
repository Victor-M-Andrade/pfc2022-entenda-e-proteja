package br.fai.ep.epWeb.service;


import br.fai.ep.epEntities.Parceiro;
import br.fai.ep.epEntities.Usuario;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public abstract class BaseWebService {
    private static final String DATE_FORMAT = "dd/MM/yyyy";
    private static final String DATE_FORMAT_COMPLETE = "dd/MM/yyyy HH:mm:ss";
    private static final String DATE_FORMAT_SAVE_FILES = "dd-MM-yyyy_HH-mm-ss";

    protected final String API_HOST = "http://localhost:8080/api";

    public static final String PATH_IMAGENS_USERS = "images" + File.separator + "users";

    public static final String dateFormate(final Timestamp time) {
        return new SimpleDateFormat(DATE_FORMAT).format(time);
    }

    public static final String dateFormateComplete(final Timestamp time) {
        return new SimpleDateFormat(DATE_FORMAT_COMPLETE).format(time);
    }

    public static final String dateFormateForSaveFiles(final Timestamp time) {
        return new SimpleDateFormat(DATE_FORMAT_SAVE_FILES).format(time);
    }

    public static Usuario anonymizeAllData(final Usuario user) {
        if (!user.isAnonimo()) {
            return user;
        }
        user.setNome(anonymizeData(user.getNome()));
        user.setEmail(anonymizeData(user.getEmail()));
        user.setSenha(anonymizeData(user.getSenha()));
        user.setPathImageProfile(anonymizeData(user.getPathImageProfile()));

        return user;
    }

    public static Parceiro anonymizeAllData(final Parceiro partner) {
        if (!partner.isAnonimo()) {
            return partner;
        }
        partner.setNome(anonymizeData(partner.getNome()));
        partner.setEmail(anonymizeData(partner.getEmail()));
        partner.setSenha(anonymizeData(partner.getSenha()));
        partner.setPathImageProfile(anonymizeData(partner.getPathImageProfile()));

        partner.setCnpj(anonymizeData(partner.getCnpj()));
        partner.setWebsite(anonymizeData(partner.getWebsite()));
        partner.setTelefone(anonymizeData(partner.getTelefone()));
        partner.setDescricao(anonymizeData(partner.getDescricao()));
        partner.setNomeEmpresa(anonymizeData(partner.getNomeEmpresa()));

        return partner;
    }

    public static String anonymizeData(final String data) {
        final char[] letters = data.toCharArray();
        if (letters.length <= 2) {
            return data;
        }

        for (int i = 2; i < data.length(); i++) {
            letters[i] = '*';
        }
        return new String(letters);
    }
}