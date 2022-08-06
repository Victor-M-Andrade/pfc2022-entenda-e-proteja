package br.fai.ep.api.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Solicitacao extends BasePojo {
    private String demanda;
    private String tipoServico;
    private Long idCliente;

    public static class RESQUEST_TABLE extends TABLE {
        public static final String TABLE_NAME = "solicitacao as S";
        public static final String SHORT_TABLE_NAME = "S.";
        public static final String DEMAND_COLUMN = "demanda";
        public static final String SERVICE_TYPE_COLUMN = "tipo_servico";
        public static final String ID_CLIENT_COLUMN = "id_cliente";
    }
}