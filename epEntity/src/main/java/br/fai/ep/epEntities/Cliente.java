package br.fai.ep.epEntities;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class Cliente extends BasePojo {
    private String nome;
    private String cnpj;
    private boolean aceite;
    private Timestamp dataHora;

    public static class CLIENT_TABLE extends TABLE {
        public static final String TABLE_NAME = "cliente as C";
        public static final String SHORT_TABLE_NAME = "C.";
        public static final String NAME_COLUMN = "nome";
        public static final String CNPJ_COLUMN = "cnpj";
        public static final String ACCEPT_COLUMN = "aceite";
        public static final String DATE_TIME_COLUMN = "data_hora";
    }
}