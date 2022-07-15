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

    public static enum TypeUser {
        ANONYMOUS("ANONIMO"),
        PARTNER("PARCEIRO"),
        AUTHOR("AUTOR"),
        DATA_OFFICER("ENCARREGADO");

        private final String name;

        private TypeUser(final String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }

}
