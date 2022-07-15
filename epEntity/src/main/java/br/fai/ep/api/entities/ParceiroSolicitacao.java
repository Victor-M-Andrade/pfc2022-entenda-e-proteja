package br.fai.ep.api.entities;

public class ParceiroSolicitacao extends BasePojo {
    private long idParceiro;
    private long idSolicitacao;

    public long getIdParceiro() {
        return idParceiro;
    }

    public void setIdParceiro(final long idParceiro) {
        this.idParceiro = idParceiro;
    }

    public long getIdSolicitacao() {
        return idSolicitacao;
    }

    public void setIdSolicitacao(final long idSolicitacao) {
        this.idSolicitacao = idSolicitacao;
    }

    public static class PARTINER_REQUEST_TABLE extends TABLE {
        public static final String TABLE_NAME = "solicitacao_parceiro as SP";
        public static final String SHORT_TABLE_NAME = "SP.";
        public static final String ID_PARTINER_COLUMN = "id_parceiro";
        public static final String ID_SOLICITATION_COLUMN = "id_solicitacao";
    }
}