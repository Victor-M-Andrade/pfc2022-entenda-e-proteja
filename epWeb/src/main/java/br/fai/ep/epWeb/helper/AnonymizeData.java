package br.fai.ep.epWeb.helper;

import br.fai.ep.epEntities.DTO.NewsDto;
import br.fai.ep.epEntities.Parceiro;
import br.fai.ep.epEntities.Usuario;

public class AnonymizeData {
    public static Usuario anonymizeAllData(final Usuario user) {
        if (!user.isAnonimo()) {
            return user;
        }
        user.setNome(anonymizeData(user.getNome()));
        user.setEmail(anonymizeData(user.getEmail()));
        user.setSenha(anonymizeData(user.getSenha()));
        user.setPathImageProfile("/resources/img/logo_invertido.png");
        return user;
    }

    public static Parceiro anonymizeAllData(final Parceiro partner) {
        if (!partner.isAnonimo()) {
            return partner;
        }
        partner.setNome(anonymizeData(partner.getNome()));
        partner.setEmail(anonymizeData(partner.getEmail()));
        partner.setSenha(anonymizeData(partner.getSenha()));
        partner.setPathImageProfile("/resources/img/logo_invertido.png");

        partner.setCnpj(anonymizeData(partner.getCnpj()));
        partner.setWebsite(anonymizeData(partner.getWebsite()));
        partner.setTelefone(anonymizeData(partner.getTelefone()));
        partner.setDescricao(anonymizeData(partner.getDescricao()));
        partner.setNomeEmpresa(anonymizeData(partner.getNomeEmpresa()));
        partner.setPathImagePartner("/resources/img/logo_invertido.png");

        return partner;
    }

    public static NewsDto anonymizeAllData(final NewsDto newsDto) {
        if (!newsDto.isAnonimo()) {
            return newsDto;
        }
        newsDto.setAuthorName(anonymizeData(newsDto.getAuthorName()));
        newsDto.setAuthorEmail(anonymizeData(newsDto.getAuthorEmail()));
        return newsDto;
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
