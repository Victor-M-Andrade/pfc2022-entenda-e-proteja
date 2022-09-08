package br.fai.ep.epEntities;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class Usuario extends BasePojo {
    private String nome;
    private String email;
    private String senha;
    private String pathImageProfile;
    private boolean aceite;
    private boolean parceiro;
    private boolean autor;
    private boolean anonimo;
    private boolean administrador;
    private Timestamp dataHora;

    public static class USER_TABLE extends TABLE {
        public static final String TABLE_NAME = "usuario as U";
        public static final String SHORT_TABLE_NAME = "U.";
        public static final String NAME_COLUMN = "nome";
        public static final String EMAIL_COLUMN = "email";
        public static final String PASSWORD_COLUMN = "senha";
        public static final String IS_AUTHOR_COLUMN = "isAutor";
        public static final String IS_PARTNER_COLUMN = "isParceiro";
        public static final String IS_ANONYMOUS_COLUMN = "isAnonimo";
        public static final String IS_ADMINISTRATOR_COLUMN = "isAdministrador";
        public static final String PATH_IMG_PROFILE = "path_img_profile";
        public static final String ACCEPT_COLUMN = "aceite";
        public static final String DATE_TIME_COLUMN = "data_hora";
    }
}