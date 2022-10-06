package br.fai.ep.epEntities;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class Noticia extends BasePojo {
    private int artigo;
    private String titulo;
    private String situacao;
    private String contexto;
    private String categoria;
    private String palavraChave;
    private Timestamp dataCriacao;
    private Timestamp dataPublicacao;
    private String pathImageNews;
    private long idAutor;
    private long idPublicador;

    public static class SITUATIONS {
        public static final String CREATED = "CRIADO";
        public static final String PUBLISHED = "PUBLICADO";
        public static final String REJECTED = "RECUSADO";
    }

    public static class NEWS_TABLE extends TABLE {
        public static final String TABLE_NAME = "noticia as N";
        public static final String SHORT_TABLE_NAME = "N.";
        public static final String TITLE_COLUMN = "titulo";
        public static final String ARTICLE_COLUMN = "artigo";
        public static final String CONTEXT_COLUMN = "contexto";
        public static final String SITUATION_COLUMN = "situacao";
        public static final String CATEGORY_COLUMN = "categoria";
        public static final String KEYWORD_COLUMN = "palavra_chave";
        public static final String CREATION_DATE_COLUMN = "data_criacao";
        public static final String PUBLICATION_DATE_COLUMN = "data_publicacao";
        public static final String PATH_IMG_NEWS = "path_img_news";
        public static final String ID_AUTHOR_COLUMN = "id_autor";
        public static final String ID_PUBLISHER_COLUMN = "id_publicador";
    }

    public static class CATOGORY {
        public static final String PASSWORDS = "senhas";
        public static final String DATA = "Armazenamento de Dados";
        public static final String LEGISLATION = "Legislação";
        public static final String LEAKAGE = "Vazamento";
        public static final String TUTORIALS = "Tutoriais";
    }
}