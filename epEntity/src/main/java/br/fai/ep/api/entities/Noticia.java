package br.fai.ep.api.entities;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
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