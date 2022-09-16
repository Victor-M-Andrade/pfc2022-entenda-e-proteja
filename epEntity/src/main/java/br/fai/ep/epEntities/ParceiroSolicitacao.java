package br.fai.ep.epEntities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParceiroSolicitacao extends BasePojo {
    private long idParceiro;
    private long idSolicitacao;

    public static class PARTINER_REQUEST_TABLE extends TABLE {
        public static final String TABLE_NAME = "solicitacao_parceiro as SP";
        public static final String SHORT_TABLE_NAME = "SP.";
        public static final String ID_PARTINER_COLUMN = "id_parceiro";
        public static final String ID_SOLICITATION_COLUMN = "id_solicitacao";
    }
}