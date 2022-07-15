package br.fai.ep.api.entities;

import java.sql.Timestamp;

public class Noticia extends BasePojo {
    private String titulo;
    private int artigo;
    private String situacao;
    private String contexto;
    private String palabraChave;
    private Timestamp dataCriacao;
    private Timestamp dataPublicao;
    private long idAutor;
    private long idPublicador;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(final String titulo) {
        this.titulo = titulo;
    }

    public int getArtigo() {
        return artigo;
    }

    public void setArtigo(final int artigo) {
        this.artigo = artigo;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(final String situacao) {
        this.situacao = situacao;
    }

    public String getPalabraChave() {
        return palabraChave;
    }

    public void setPalabraChave(final String palabraChave) {
        this.palabraChave = palabraChave;
    }

    public Timestamp getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(final Timestamp dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Timestamp getDataPublicao() {
        return dataPublicao;
    }

    public void setDataPublicao(final Timestamp dataPublicao) {
        this.dataPublicao = dataPublicao;
    }

    public long getIdAutor() {
        return idAutor;
    }

    public void setIdAutor(final long idAutor) {
        this.idAutor = idAutor;
    }

    public long getIdPublicador() {
        return idPublicador;
    }

    public void setIdPublicador(final long idPublicador) {
        this.idPublicador = idPublicador;
    }

    public String getContexto() {
        return contexto;
    }

    public void setContexto(final String contexto) {
        this.contexto = contexto;
    }

    public static enum NewsSituation {
        CREATED("CRIADO"),
        PUBLISHED("PUBLICADO"),
        REJECTED("RECUSADO");

        private final String name;

        private NewsSituation(final String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }

    public static class NEWS_TABLE extends TABLE {
        public static final String TABLE_NAME = "noticia as N";
        public static final String SHORT_TABLE_NAME = "N.";
        public static final String TITLE_COLUMN = "titulo";
        public static final String ARTICLE_COLUMN = "artigo";
        public static final String CONTEXT_COLUMN = "contexto";
        public static final String SITUATION_COLUMN = "situacao";
        public static final String KEYWORD_COLUMN = "palavra_chave";
        public static final String CREATION_DATE_COLUMN = "data_criacao";
        public static final String PUBLICATION_DATE_COLUMN = "data_publicacao";
        public static final String ID_AUTHOR_COLUMN = "id_autor";
        public static final String ID_PUBLISHER_COLUMN = "id_publicador";
    }
}