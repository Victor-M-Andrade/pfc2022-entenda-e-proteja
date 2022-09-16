package br.fai.ep.epEntities;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class Teste extends BasePojo {
    private Timestamp dataHora;
    private int acertos;
    private long idUsuario;

    public static class TEST_TABLE extends TABLE {
        public static final String TABLE_NAME = "teste as T";
        public static final String SHORT_TABLE_NAME = "T.";
        public static final String DATE_TIME_COLUMN = "data_hora";
        public static final String HIT_COLUMN = "qtd_acerto";
        public static final String ID_USER_COLUMN = "id_usuario";
    }
}