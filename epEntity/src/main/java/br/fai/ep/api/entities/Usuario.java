package br.fai.ep.api.entities;

import java.sql.Timestamp;

public class Usuario extends BasePojo {
    private String nome;
    private String tipo;
    private String email;
    private String senha;
    private boolean aceite;
    private Timestamp dataHora;

    public String getNome() {
        return nome;
    }

    public void setNome(final String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(final String tipo) {
        this.tipo = tipo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(final String senha) {
        this.senha = senha;
    }

    public boolean isAceite() {
        return aceite;
    }

    public void setAceite(final boolean aceite) {
        this.aceite = aceite;
    }

    public Timestamp getDataHora() {
        return dataHora;
    }

    public void setDataHora(final Timestamp dataHora) {
        this.dataHora = dataHora;
    }

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