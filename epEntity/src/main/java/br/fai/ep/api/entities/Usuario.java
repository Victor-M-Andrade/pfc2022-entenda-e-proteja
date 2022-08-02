package br.fai.ep.api.entities;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class Usuario extends BasePojo {
    private String nome;
    private String tipo;
    private String email;
    private String senha;
    private boolean aceite;
    private Timestamp dataHora;

    public static class USER_TABLE extends TABLE {
        public static final String TABLE_NAME = "usuario as U";
        public static final String SHORT_TABLE_NAME = "U.";
        public static final String NAME_COLUMN = "nome";
        public static final String TYPE_COLUMN = "tipo";
        public static final String EMAIL_COLUMN = "email";
        public static final String PASSWORD_COLUMN = "senha";
        public static final String ACCEPT_COLUMN = "aceite";
        public static final String DATE_TIME_COLUMN = "data_hora";
    }

    public static class USERS_TYPE {
        public static final String INTERESTED_PERSON = "INTERESSADO";
        public static final String AUTHOR = "AUTOR";
        public static final String DATA_OFFICER = "ENCARREGADO";
        public static final String PARTNER = "PARCEIRO";
        public static final String ADMINISTRATO = "ADM";

    }
}