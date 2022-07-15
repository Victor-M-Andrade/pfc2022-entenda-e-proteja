package br.fai.ep.api.db.helper;

public abstract class DataBaseHelper {

    public enum UserTable {
        TABLE_NAME("usuario as U"),
        SHORT_TABLE_NAME("U."),
        ID_COLUMN("id"),
        NAME_COLUMN("nome"),
        TYPE_COLUMN("tipo"),
        EMAIL_COLUMN("email"),
        PASSWORD_COLUMN("senha"),
        ACCEPT_COLUMN("aceite"),
        DATE_TIME_COLUMN("data_hora");

        private final String name;

        UserTable(final String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }

    public enum PartinerTable {
        TABLE_NAME("parceiro as P"),
        SHORT_TABLE_NAME("P."),
        ID_COLUMN("id"),
        CNPJ_COLUMN("cnpj"),
        WEBSITE_COLUMN("site_parceiro"),
        SITUATION_COLUMN("situacao"),
        DESCRIPTION_COLUMN("descricao"),
        SERVICE_TYPE_COLUMN("tipo_servico"),
        COMPANY_NAME_COLUMN("nome_empresa"),
        ID_USER_COLUMN("id_usuario");

        private final String name;

        PartinerTable(final String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }

    public enum ClientTable {
        TABLE_NAME("cliente as C"),
        SHORT_TABLE_NAME("C."),
        ID_COLUMN("id"),
        NAME_COLUMN("nome"),
        CNPJ_COLUMN("cnpj"),
        ACCEPT_COLUMN("aceite"),
        DATE_TIME_COLUMN("data_hora");

        private final String name;

        ClientTable(final String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }

    public enum RequestTable {
        TABLE_NAME("solicitacao as S"),
        SHORT_TABLE_NAME("S."),
        ID_COLUMN("id"),
        DEMAND_COLUMN("demanda"),
        SERVICE_TYPE_COLUMN("tipo_servico"),
        ID_CLIENT_COLUMN("id_cliente");

        private final String name;

        RequestTable(final String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }

    public enum PartinerRequestTable {
        TABLE_NAME("solicitacao_parceiro as SP"),
        SHORT_TABLE_NAME("SP."),
        ID_COLUMN("id"),
        ID_PARTINER_COLUMN("id_parceiro"),
        ID_SOLICITATION_COLUMN("id_solicitacao");

        private final String name;

        private PartinerRequestTable(final String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }

    public enum NewsTable {
        TABLE_NAME("noticia as N"),
        SHORT_TABLE_NAME("N."),
        ID_COLUMN("id"),
        TITLE_COLUMN("titulo"),
        ARTICLE_COLUMN("artigo"),
        CONTEXT_COLUMN("contexto"),
        SITUATION_COLUMN("situacao"),
        KEYWORD_COLUMN("palavra_chave"),
        CREATION_DATE_COLUMN("data_criacao"),
        PUBLICATION_DATE_COLUMN("data_publicacao"),
        ID_AUTHOR_COLUMN("id_autor"),
        ID_PUBLISHER_COLUMN("id_publicador");

        private final String name;

        NewsTable(final String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }

    public enum QuestionTable {
        TABLE_NAME("questao as Q"),
        SHORT_TABLE_NAME("Q."),
        ID_COLUMN("id"),
        QUESTION_COLUMN("pergunta"),
        ALTERNATIVE_A_COLUMN("alternativa_a"),
        ALTERNATIVE_B_COLUMN("alternativa_b"),
        ALTERNATIVE_C_COLUMN("alternativa_c"),
        ALTERNATIVE_D_COLUMN("alternativa_d"),
        ANSWER_COLUMN("resposta"),
        LEVEL_COLUMN("nivel");

        private final String name;

        QuestionTable(final String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }

    public enum TestTable {
        TABLE_NAME("teste as T"),
        SHORT_TABLE_NAME("T."),
        ID_COLUMN("id"),
        DATE_TIME_COLUMN("data_hora"),
        HIT_COLUMN("qtd_acerto"),
        ID_USER_COLUMN("id_usuario");

        private final String name;

        private TestTable(final String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }

    public enum QuestionTestTable {
        TABLE_NAME("teste_questao as TQ"),
        SHORT_TABLE_NAME("TQ."),
        ID_COLUMN("id"),
        CHOICE_COLUMN("escolha"),
        ID_QUEST_COLUMN("id_questao"),
        ID_TEST_COLUMN("id_teste");

        private final String name;

        QuestionTestTable(final String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }

    public enum Sql {
        SELECT_FULL("SELECT * FROM "),
        INSERT("INSERT INTO "),
        VALUES(" VALUES("),
        UPDATE("UPDATE "),
        SET_UPDATE(" SET "),
        DELETE("DELETE FROM "),
        WHERE(" WHERE "),
        SEPARATOR(", "),
        OPEN_PARENTHESIS("("),
        CLOSE_PARENTHESIS(") "),
        FINAL_CLOSE_PARENTHESIS(");"),
        DEFAULT_VALUE_DECLARTION("default"),
        PARAM_INSERT_TO_COMPLETE("?, "),
        LAST_PARAM_INSERT_TO_COMPLETE("?); "),
        PARAM_UPDATE_TO_COMPLETE("= ?, "),
        lAST_PARAM_UPDATE_TO_COMPLETE(" = ?"),
        INNER_JOIN(" INNER JOIN "),
        EQUAL_COMPATION(" = "),
        AND(" AND "),
        OR(" OR "),
        LIKE(" LIKE ");

        private final String name;

        Sql(final String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }
}
